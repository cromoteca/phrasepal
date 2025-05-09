import { configureAuth } from '@vaadin/hilla-react-auth';
import { UserService } from 'Frontend/generated/endpoints';

// Configure auth to use `UserInfoService.getUserInfo`
const auth = configureAuth(UserService.getCurrentUser);

// Export auth provider and useAuth hook, which are automatically
// typed to the result of `UserInfoService.getUserInfo`
export const useAuth = auth.useAuth;
export const AuthProvider = auth.AuthProvider;
