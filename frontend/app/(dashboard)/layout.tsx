// app/(dashboard)/layout.tsx
import React from 'react';

export default function DashboardLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
    <body>
    <div className="flex">
      <aside className="w-64 border-r p-4">Dashboard 侧边栏</aside>
      <main className="flex-1 p-4">{children}</main>
    </div>
    </body>
    </html>
  );
}
