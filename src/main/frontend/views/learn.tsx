import { useSignal } from '@vaadin/hilla-react-signals';
import { TextArea, Button, FormLayout } from '@vaadin/react-components';
import { TranslationToTargetLanguageService } from 'Frontend/generated/endpoints';
import { sourceLanguage, targetLanguage } from './@layout';
import { useEffect } from 'react';

export default function LearnView() {
    const inputText = useSignal('Where can I find a cash machine?');
    const translatedText = useSignal('');
    const voice = useSignal<SpeechSynthesisVoice | undefined>();

    const handleTranslateClick = () => {
        TranslationToTargetLanguageService.translateToTargetLanguage(
            inputText.value,
            sourceLanguage.value.label,
            targetLanguage.value.label
        ).then(translation => translation && (translatedText.value = translation));
    };

    useEffect(() => {
        const voices = speechSynthesis.getVoices();
        voice.value = voices.find(voice => voice.lang === targetLanguage.value.value);
    }, [targetLanguage.value]);

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
                    onChange={(e) => inputText.value = e.target.value}
                />
                <Button theme="primary" onClick={handleTranslateClick}>Translate</Button>
                <TextArea
                    label="Translated text"
                    value={translatedText.value}
                    readonly
                />
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
