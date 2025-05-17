import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { useSignal } from '@vaadin/hilla-react-signals';
import { WordService } from 'Frontend/generated/endpoints';
import { useEffect } from 'react';
import { currentUser } from './@layout';

export const config: ViewConfig = {
  loginRequired: true,
  menu: {
    order: 20,
  },
};

export default function PracticeView() {
  const words = useSignal<string[]>([]);

  useEffect(() => {
    WordService.getWordsForUser(currentUser.value).then((result) => {
      if (result) {
        words.value = result.map((word) => word!.word!);
      }
    });
  }, [currentUser.value]);

  return (
    <div className="p-m">
      <h3>Practice</h3>
      <p>This page will allow you to practice your language skills.</p>
      {words.value.length > 0 ? (
        <>
          <p>Based on your activity, here are some words you will be invited to practice:</p>
          <ul>
            {words.value.map((word) => (
              <li key={word}>{word}</li>
            ))}
          </ul>
        </>
      ) : (
        <p>You should first learn some phrases, so that the application can detect the words you want to practice.</p>
      )}
    </div>
  );
}
