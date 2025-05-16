import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { translate } from '@vaadin/hilla-react-i18n';
export const config: ViewConfig = {
  menu: {
    order: 0,
  },
};

export default function HomeView() {
  return <div className="p-m">{translate('home.welcome')}</div>;
}
