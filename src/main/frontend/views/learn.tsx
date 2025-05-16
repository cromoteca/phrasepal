import { useSignal } from '@vaadin/hilla-react-signals';
import { Button, FormLayout, TextArea } from '@vaadin/react-components';
import { TranslationToTargetLanguageService } from 'Frontend/generated/endpoints';
import { currentUser, voice } from './@layout';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { translate } from '@vaadin/hilla-react-i18n';

export const config: ViewConfig = {
  loginRequired: true,
};

export default function LearnView() {
  const inputText = useSignal('');
  const translatedText = useSignal('');

  const handleTranslateClick = () => {
    translatedText.value = '';

    TranslationToTargetLanguageService.translateToTargetLanguage(
      inputText.value,
      currentUser.value!.spokenLanguage!.name,
      currentUser.value!.studiedLanguage!.name,
    ).onNext((chunk) => {
      if (chunk) {
        translatedText.value += chunk;
      }
    });
  };

  const playTranslation = (rate = 1) => {
    const utterance = new SpeechSynthesisUtterance(translatedText.value);
    if (voice.value) {
      utterance.voice = voice.value;
    }
    utterance.rate = rate;
    speechSynthesis.speak(utterance);
  };

  const handlePlayTranslation = () => {
    playTranslation();
  };

  const handlePlayTranslationSlow = () => {
    playTranslation(0.5);
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
