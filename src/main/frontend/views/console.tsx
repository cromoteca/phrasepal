import { AutoGrid } from '@vaadin/hilla-react-crud';
import { VerticalLayout } from '@vaadin/react-components';
import WordModel from 'Frontend/generated/com/cromoteca/phrasepal/words/WordModel';
import { WordService } from 'Frontend/generated/endpoints';

export default function Console() {
  return (
    <VerticalLayout>
      <h1>Console</h1>
      <AutoGrid
        model={WordModel}
        service={WordService}
        visibleColumns={['word', 'user.email', 'language.name', 'failedUsages', 'successfulUsages']}
      />
    </VerticalLayout>
  );
}
