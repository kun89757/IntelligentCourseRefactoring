export default interface User {
  readonly id: number;
  username: string;
  avatar: string;
  email: string;
  blocked: boolean;
  phone?: string;
  roleId: number;
}
