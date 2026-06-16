import { type ReactNode } from 'react';

interface CardProps {
  children: ReactNode;
  className?: string;
  hover?: boolean;
  onClick?: () => void;
}

export function Card({ children, className = '', hover = false, onClick }: CardProps) {
  return (
    <div
      role={onClick ? 'button' : undefined}
      tabIndex={onClick ? 0 : undefined}
      onClick={onClick}
      onKeyDown={onClick ? (e) => e.key === 'Enter' && onClick() : undefined}
      className={`rounded-xl border border-wood-100 bg-white p-6 shadow-sm ${hover ? 'cursor-pointer transition-all duration-300 hover:-translate-y-1 hover:shadow-md' : ''} ${className}`}
    >
      {children}
    </div>
  );
}
