"use client"

import React from 'react';
import User from '@/types/user';
import { Button } from '@/components/ui/button';
import {
  DropdownMenu,
  DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem,
  DropdownMenuLabel, DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu';
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar';
import { assets } from '@/assets/assets.js';


interface HeaderProps {
  user: User
}

const Header = ({ user }: HeaderProps) => {

  const avatar: string = user ? user.avatar : 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png';
  const avatarFallback: string = user ? user.username : 'U';

  const toCourseList = (): void => {
    //TODO
  }

  const toConceptList = (): void => {
    // TODO
  }

  const toProfile = (user: User): void => {
    //TODO
  }

  const toMyServices = (id: number): void => {
    //TODO
  }

  const toSettings = (id: number): void => {
    //TODO
  }

  const toBillings = (id: number): void => {
    //TODO
  }

  const logout = (): void => {
    //TODO
  }

  const login = (): void => {
    //TODO
  }


  return (
    <>
      <div id={ 'container' } className={'relative'}>
        <div className={ 'heading' }>
          <Avatar className={'ml-[20%]'}>
            <AvatarImage src={assets.logo_icon.src} alt={'logo'}></AvatarImage>
            <AvatarFallback>LOGO</AvatarFallback>
          </Avatar>
          {
            (user && user.roleId === 0) &&
            <div className={'ml-[5%]'}>
              <Button variant={ 'ghost' } className={ 'font-black' } onClick={ () => toCourseList() }>
                Courses
              </Button>
              <Button variant={ 'ghost' } className={ 'font-black' } onClick={ () => toConceptList() }>
                Concepts
              </Button>
            </div>
          }
          <DropdownMenu>
            <DropdownMenuTrigger asChild className={'absolute right-1/5'}>
              <Avatar className={'cursor-pointer'}>
                <AvatarImage src={ avatar } alt={ 'avatar' }></AvatarImage>
                <AvatarFallback>{ avatarFallback }</AvatarFallback>
              </Avatar>
            </DropdownMenuTrigger>
            {
              user ?
                <DropdownMenuContent>
                  <DropdownMenuLabel>
                    My Account
                  </DropdownMenuLabel>
                  <DropdownMenuSeparator/>
                  <DropdownMenuGroup>
                    <DropdownMenuItem
                      onClick={ () => toProfile(user) }>
                      Profile
                    </DropdownMenuItem>
                    {
                      user.roleId === 0 &&
                      <>
                        <DropdownMenuItem
                          onClick={ () => toMyServices(user.id) }>
                          My Services
                        </DropdownMenuItem>
                        <DropdownMenuItem
                          onClick={ () => toBillings(user.id) }>
                          Billings
                        </DropdownMenuItem>
                        <DropdownMenuItem
                          onClick={ () => toSettings(user.id) }>
                          Settings
                        </DropdownMenuItem>
                      </>
                    }
                  </DropdownMenuGroup>
                  <DropdownMenuSeparator/>
                  <DropdownMenuItem
                    onClick={ () => logout() }>
                    Logout
                  </DropdownMenuItem>
                </DropdownMenuContent> :
                <DropdownMenuContent>
                  <DropdownMenuItem
                    onClick={() => login()}>
                    Login
                  </DropdownMenuItem>
                </DropdownMenuContent>
            }

          </DropdownMenu>
        </div>
      </div>
    </>
  );
};

export default Header;
