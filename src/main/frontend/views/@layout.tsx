import { createMenuItems, useViewConfig } from '@vaadin/hilla-file-router/runtime.js';
import { i18n, translate } from '@vaadin/hilla-react-i18n';
import { effect, signal, useComputed } from '@vaadin/hilla-react-signals';
import { AppLayout, Button, DrawerToggle, Icon, Select, SideNav, SideNavItem } from '@vaadin/react-components';
import { useAuth } from 'Frontend/auth/auth';
import Language from 'Frontend/generated/com/cromoteca/phrasepal/languages/Language';
import User from 'Frontend/generated/com/cromoteca/phrasepal/user/User';
import { LanguageService, UserService } from 'Frontend/generated/endpoints';
import { Suspense, useEffect } from 'react';
import { Outlet, useLocation, useNavigate } from 'react-router';

const documentTitleSignal = signal('');
const languages = signal<Language[]>([]);
const items = signal<{ value: string; label: string }[]>([]);
const languageSelectionTranslation = signal('');
export const voice = signal<SpeechSynthesisVoice | undefined>();
export const currentUser = signal<User | undefined>();

effect(() => {
  i18n.configure();
});

LanguageService.getAllLanguages().then((result) => {
  languages.value = result;
});
UserService.getCurrentUser().then((result) => (currentUser.value = result));

effect(() => {
  const userStudiedLanguage = currentUser.value?.studiedLanguage;

  if (userStudiedLanguage) {
    const voices = speechSynthesis.getVoices();
    voice.value = voices.find((voice) => voice.lang === userStudiedLanguage.code);
  }

  const userSpokenLanguage = currentUser.value?.spokenLanguage;
  userSpokenLanguage?.code &&
    i18n.setLanguage(userSpokenLanguage.code).then(() => {
      languageSelectionTranslation.value = translate('home.languages', {
        spokenLanguage: 'L1',
        studiedLanguage: 'L2',
      });
    });

  items.value = languages.value.map((lang) => ({
    value: lang.code!,
    label: `${lang.flag} ${translate('language.' + lang.code!.substring(0, 2))}`,
  }));
});

effect(() => {
  document.title = documentTitleSignal.value;
});

// Publish for Vaadin to use
(window as any).Vaadin.documentTitleSignal = documentTitleSignal;

export default function MainLayout() {
  const currentTitle = useViewConfig()?.title;
  const navigate = useNavigate();
  const location = useLocation();
  const { state, logout } = useAuth();

  useEffect(() => {
    if (currentTitle) {
      documentTitleSignal.value = currentTitle;
    }
  }, [currentTitle]);

  const languageSelectionComponents = useComputed(() => {
    const parts = languageSelectionTranslation.value.split(/(L1|L2)/).filter((part) => part.trim() !== '');
    return parts.map((part) => {
      if (part === 'L1') {
        return (
          <Select
            key={part}
            items={items.value}
            value={currentUser.value?.spokenLanguage?.code}
            onValueChanged={({ detail: { value } }) => {
              const selectedLanguage = languages.value.find((lang) => lang.code === value);
              if (selectedLanguage?.code && selectedLanguage.code !== currentUser.value?.spokenLanguage?.code) {
                UserService.updateSpokenLanguage(currentUser.value!.id!, selectedLanguage).then((updatedUser) => {
                  currentUser.value = updatedUser;
                });
              }
            }}
          />
        );
      } else if (part === 'L2') {
        return (
          <Select
            key={part}
            items={items.value}
            value={currentUser.value?.studiedLanguage?.code}
            onValueChanged={({ detail: { value } }) => {
              const selectedLanguage = languages.value.find((lang) => lang.code === value);
              if (selectedLanguage?.code && selectedLanguage.code !== currentUser.value?.studiedLanguage?.code) {
                UserService.updateStudiedLanguage(currentUser.value!.id!, selectedLanguage).then((updatedUser) => {
                  currentUser.value = updatedUser;
                });
              }
            }}
          />
        );
      }
      return <span key={part}>{part}</span>;
    });
  });

  return (
    <AppLayout primarySection="drawer">
      <div slot="drawer" className="flex flex-col justify-between h-full p-m">
        <header className="flex flex-col gap-m">
          <span className="font-semibold text-l">Phrase Pal</span>
          <SideNav onNavigate={({ path }) => navigate(path!)} location={location}>
            {createMenuItems().map(({ to, title, icon }) => (
              <SideNavItem path={to} key={to}>
                {icon ? <Icon src={icon} slot="prefix"></Icon> : <></>}
                {title}
              </SideNavItem>
            ))}
          </SideNav>
        </header>
        <footer className="flex flex-col gap-s">
          {state.user ? (
            <>
              <div className="flex items-center gap-s">{state.user.email}</div>
              <Button
                onClick={async () => {
                  await logout();
                  document.location.reload();
                }}>
                {translate('home.signOut')}
              </Button>
            </>
          ) : (
            <Button onClick={() => window.location.replace('/login')}>{translate('home.signIn')}</Button>
          )}
        </footer>
      </div>

      <DrawerToggle slot="navbar" aria-label="Menu toggle"></DrawerToggle>
      <h1 slot="navbar" className="text-l m-0">
        {documentTitleSignal}
      </h1>

      <Suspense>
        <div className="p-m">{languageSelectionComponents}</div>
        <Outlet />
      </Suspense>
    </AppLayout>
  );
}
