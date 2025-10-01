// app/(root)/layout.tsx
import "@/app/globals.css";
import React from 'react';
import HeaderWrapper from '@/app/components/HeadWrapper';

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
    <body>
    <HeaderWrapper></HeaderWrapper>
    <main>{children}</main>
    </body>
    </html>
  );
}
