/**
 * Button 컴포넌트 타입 정의
 */

export type ButtonVariant = 
  | 'primary' 
  | 'secondary' 
  | 'tertiary'
  | 'danger' 
  | 'outline' 
  | 'ghost'
  | 'text'

export type ButtonSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl'

export type ButtonType = 'button' | 'submit' | 'reset'

export type ButtonTag = 'button' | 'a' | 'router-link'

export interface ButtonProps {
  type?: ButtonType
  tag?: ButtonTag
  href?: string
  to?: string | object
  disabled?: boolean
  variant?: ButtonVariant
  size?: ButtonSize
  loading?: boolean
  loadingText?: string
  icon?: string
  iconRight?: boolean
  fullWidth?: boolean
  rounded?: boolean
}

