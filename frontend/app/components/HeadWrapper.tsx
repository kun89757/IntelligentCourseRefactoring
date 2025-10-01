import Header from "./Header";
import User from '@/types/user';

export default async function HeaderWrapper() {

  // TODO
  const user: User = {
    id: 1,
    username: 'Purpurrot',
    nickname: 'Purpurrot',
    email: 'kun.l01101100@gmail.com',
    roleId: 0,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }

  const admin: User = {
    id: 2,
    username: 'Nuwanda',
    nickname: 'Nuwanda',
    email: 'a125516524@163.com',
    roleId: 2,
    avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  }

  // const res = await fetch("http://localhost:8080/api/users/me", {
  //   cache: "no-store", // 确保每次请求最新头像
  //   headers: {
  //     Authorization: "Bearer token", // 真实项目中放 JWT
  //   },
  // });
  // const user = await res.json();

  return <Header user={user} />;
}
