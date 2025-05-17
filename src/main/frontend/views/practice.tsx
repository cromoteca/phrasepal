import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { translate } from '@vaadin/hilla-react-i18n';
import { useSignal } from '@vaadin/hilla-react-signals';
import { Button, FormLayout, TextArea } from '@vaadin/react-components';
import PhraseWithWords from 'Frontend/generated/com/cromoteca/phrasepal/ai/AIService/PhraseWithWords';
import { AIService } from 'Frontend/generated/endpoints';
import { useEffect } from 'react';
import { currentUser } from './@layout';

export const config: ViewConfig = {
  loginRequired: true,
  menu: {
    order: 20,
  },
};

export default function PracticeView() {
  const phrase = useSignal<PhraseWithWords>({ phrase: '', words: [] });
  const userTranslation = useSignal<string>('');
  const correction = useSignal<string>('');

  useEffect(() => {
    AIService.generatePhrase().then((p) => {
      phrase.value = p;
    });
  }, [currentUser.value]);

  function handleCheckTranslation() {
    AIService.checkTranslation(
      phrase.value.phrase,
      userTranslation.value,
      phrase.value.words,
      currentUser.value!.studiedLanguage!.name,
      currentUser.value!.spokenLanguage!.name,
    ).then((c) => {
      c && (correction.value = c);
    });
  }

  return (
    <div className="p-m">
      <h3>Practice</h3>
      <FormLayout responsiveSteps={[{ minWidth: 0, columns: 1 }]}>
        <TextArea label={translate('practice.phrase')} value={phrase.value.phrase} readonly />
        <TextArea
          label={translate('practice.translation')}
          placeholder={translate('practice.translationPlaceholder')}
          value={userTranslation.value}
          onChange={(e) => (userTranslation.value = e.target.value)}
        />
        <Button onClick={handleCheckTranslation}>{translate('practice.check')}</Button>
        <TextArea label={translate('practice.correction')} value={correction.value} readonly />
      </FormLayout>
    </div>
  );
}
