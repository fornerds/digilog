/**
 * 폼 유효성 검사 유틸리티 함수
 */

export interface ValidationResult {
  isValid: boolean
  error?: string
}

/**
 * 이름 유효성 검사
 */
export const validateName = (name: string): ValidationResult => {
  if (!name || name.trim().length === 0) {
    return { isValid: false, error: '이름을 입력해주세요' }
  }
  
  if (name.trim().length < 2) {
    return { isValid: false, error: '이름은 2자 이상 입력해주세요' }
  }
  
  if (name.length > 20) {
    return { isValid: false, error: '이름은 20자 이하로 입력해주세요' }
  }
  
  // 한글, 영문, 공백만 허용
  const nameRegex = /^[가-힣a-zA-Z\s]+$/
  if (!nameRegex.test(name)) {
    return { isValid: false, error: '이름은 한글 또는 영문만 입력 가능합니다' }
  }
  
  return { isValid: true }
}

/**
 * 이메일 유효성 검사
 */
export const validateEmail = (email: string): ValidationResult => {
  if (!email || email.trim().length === 0) {
    return { isValid: false, error: '이메일을 입력해주세요' }
  }
  
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailRegex.test(email)) {
    return { isValid: false, error: '올바른 이메일 형식을 입력해주세요' }
  }
  
  return { isValid: true }
}

/**
 * 휴대폰번호 유효성 검사
 */
export const validatePhone = (phone: string): ValidationResult => {
  if (!phone || phone.trim().length === 0) {
    return { isValid: false, error: '휴대폰번호를 입력해주세요' }
  }
  
  // 하이픈 제거 후 숫자만 추출
  const numbers = phone.replace(/\D/g, '')
  
  if (numbers.length !== 11) {
    return { isValid: false, error: '휴대폰번호는 11자리 숫자로 입력해주세요' }
  }
  
  // 010으로 시작하는지 확인
  if (!numbers.startsWith('010')) {
    return { isValid: false, error: '010으로 시작하는 번호를 입력해주세요' }
  }
  
  return { isValid: true }
}

/**
 * 생년월일 유효성 검사
 */
export const validateBirthDate = (birthDate: string): ValidationResult => {
  if (!birthDate || birthDate.trim().length === 0) {
    return { isValid: false, error: '생년월일을 선택해주세요' }
  }
  
  const date = new Date(birthDate)
  const today = new Date()
  
  // 미래 날짜는 불가
  if (date > today) {
    return { isValid: false, error: '미래 날짜는 선택할 수 없습니다' }
  }
  
  // 100년 이상 된 날짜는 불가
  const hundredYearsAgo = new Date()
  hundredYearsAgo.setFullYear(today.getFullYear() - 100)
  
  if (date < hundredYearsAgo) {
    return { isValid: false, error: '올바른 생년월일을 선택해주세요' }
  }
  
  return { isValid: true }
}

/**
 * 비밀번호 유효성 검사
 */
export const validatePassword = (password: string): ValidationResult => {
  if (!password || password.length === 0) {
    return { isValid: false, error: '비밀번호를 입력해주세요' }
  }
  
  if (password.length < 8) {
    return { isValid: false, error: '비밀번호는 8자 이상 입력해주세요' }
  }
  
  if (password.length > 20) {
    return { isValid: false, error: '비밀번호는 20자 이하로 입력해주세요' }
  }
  
  // 영문, 숫자, 특수문자 중 2가지 이상 포함
  const hasLetter = /[a-zA-Z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password)
  
  const typeCount = [hasLetter, hasNumber, hasSpecial].filter(Boolean).length
  
  if (typeCount < 2) {
    return { isValid: false, error: '비밀번호는 영문, 숫자, 특수문자 중 2가지 이상을 포함해주세요' }
  }
  
  return { isValid: true }
}

/**
 * 비밀번호 확인 유효성 검사
 */
export const validatePasswordConfirm = (
  password: string,
  passwordConfirm: string
): ValidationResult => {
  if (!passwordConfirm || passwordConfirm.length === 0) {
    return { isValid: false, error: '비밀번호 확인을 입력해주세요' }
  }
  
  if (password !== passwordConfirm) {
    return { isValid: false, error: '비밀번호가 일치하지 않습니다' }
  }
  
  return { isValid: true }
}





