import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { AutoGrid } from '@vaadin/hilla-react-crud';
import { VerticalLayout } from '@vaadin/react-components';
import WordModel from 'Frontend/generated/com/cromoteca/phrasepal/words/WordModel';
import { WordService } from 'Frontend/generated/endpoints';

export const config: ViewConfig = {
  loginRequired: true,
  menu: {
    order: 30,
  },
};

export default function Console() {
  return (
    <div className="p-m">
      <VerticalLayout>
        <h3>Console</h3>
        <p>A quick way to see the words in the database.</p>
        <AutoGrid
          model={WordModel}
          service={WordService}
          visibleColumns={['word', 'user.email', 'language.name', 'failedUsages', 'successfulUsages']}
        />
      </VerticalLayout>
    </div>
  );
}
