import { TextArea, Button, FormLayout } from '@vaadin/react-components';

export default function LearnView() {
    return (
        <div className="p-m">
            <h1>Translate a Phrase</h1>
            <FormLayout responsiveSteps={[{ minWidth: 0, columns: 1 }]}>
                <TextArea label="Enter phrase" placeholder="Type a phrase here..." />
                <Button theme="primary">Translate</Button>
                <TextArea label="Translated text" value="Translated text will appear here..." readonly />
            </FormLayout>
        </div>
    );
}
