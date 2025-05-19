import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { translate } from '@vaadin/hilla-react-i18n';
import { useSignal } from '@vaadin/hilla-react-signals';
import { Button, FormLayout, TextArea } from '@vaadin/react-components';
import { AIService } from 'Frontend/generated/endpoints';
import { currentUser, playText } from './@layout';

export const config: ViewConfig = {
  loginRequired: true,
  menu: {
    order: 10,
  },
};

export default function LearnView() {
  const inputText = useSignal('');
  const translatedText = useSignal('');

  const handleTranslateClick = () => {
    translatedText.value = '';

    AIService.translateToTargetLanguage(
      inputText.value,
      currentUser.value!.spokenLanguage!.name,
      currentUser.value!.studiedLanguage!.name,
    ).onNext((chunk) => {
      chunk && (translatedText.value += chunk);
    });
  };

  const handlePlayTranslation = () => {
    playText(translatedText.value);
  };

  const handlePlayTranslationSlow = () => {
    playText(translatedText.value, 0.5);
  };

  return (
    <div className="p-m">
      <h3>{translate('learn.welcome')}</h3>
      <FormLayout responsiveSteps={[{ minWidth: 0, columns: 1 }]}>
        <TextArea
          label={translate('learn.input')}
          placeholder={translate('learn.inputPlaceholder')}
          value={inputText.value}
          onChange={(e) => (inputText.value = e.target.value)}
        />
        <Button theme="primary" onClick={handleTranslateClick}>
          {translate('learn.translate')}
        </Button>
        <TextArea label={translate('learn.translatedText')} value={translatedText.value} readonly />
        <Button theme="secondary" onClick={handlePlayTranslation} disabled={!translatedText.value}>
          {translate('learn.playTranslation')}
        </Button>
        <Button theme="secondary" onClick={handlePlayTranslationSlow} disabled={!translatedText.value}>
          {translate('learn.playTranslationSlow')}
        </Button>
      </FormLayout>
    </div>
  );
}
