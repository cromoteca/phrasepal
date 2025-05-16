import { ViewConfig } from '@vaadin/hilla-file-router/types.js';

export const config: ViewConfig = {
  loginRequired: true,
  menu: {
    order: 20,
  },
};

export default function PracticeView() {
  return (
    <div className="p-m">
      <h3>Practice</h3>
      <p>This page will allow you to practice your language skills.</p>
    </div>
  );
}
