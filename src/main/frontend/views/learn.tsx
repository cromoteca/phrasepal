import { useComputed, useSignal } from '@vaadin/hilla-react-signals';
import { Button, FormLayout, TextArea } from '@vaadin/react-components';
import { TranslationToTargetLanguageService } from 'Frontend/generated/endpoints';
import { currentUser, voice } from './@layout';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  loginRequired: true,
};

export default function LearnView() {
  const inputText = useSignal('Je dois acheter un billet de train');
  const translatedText = useSignal('');

  const handleTranslateClick = () => {
    TranslationToTargetLanguageService.translateToTargetLanguage(
      inputText.value,
      currentUser.value!.spokenLanguage!.name,
      currentUser.value!.studiedLanguage!.name,
    ).then((translation) => translation && (translatedText.value = translation));
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
      <h3>What would you like to learn?</h3>
      <FormLayout responsiveSteps={[{ minWidth: 0, columns: 1 }]}>
        <TextArea
          label="Enter phrase"
          placeholder="Type a phrase here..."
          value={inputText.value}
          onChange={(e) => (inputText.value = e.target.value)}
        />
        <Button theme="primary" onClick={handleTranslateClick}>
          Translate
        </Button>
        <TextArea label="Translated text" value={translatedText.value} readonly />
        <Button theme="secondary" onClick={handlePlayTranslation} disabled={!translatedText.value}>
          Play Translation
        </Button>
        <Button theme="secondary" onClick={handlePlayTranslationSlow} disabled={!translatedText.value}>
          Play Translation Slowly
        </Button>
      </FormLayout>
    </div>
  );
}
