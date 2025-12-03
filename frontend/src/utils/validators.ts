/**
 * 이메일 유효성 검사
 */
export const validateEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  return emailRegex.test(email)
}

/**
 * 비밀번호 유효성 검사 (최소 8자)
 */
export const validatePassword = (password: string): boolean => {
  return password.length >= 8
}

/**
 * 휴대폰 번호 유효성 검사
 */
export const validatePhone = (phone: string): boolean => {
  // 010-1234-5678 또는 01012345678 형식
  const phoneRegex = /^010-?\d{4}-?\d{4}$/
  return phoneRegex.test(phone)
}

/**
 * 생년월일 유효성 검사 (YYYY-MM-DD)
 */
export const validateBirthDate = (birthDate: string): boolean => {
  const dateRegex = /^\d{4}-\d{2}-\d{2}$/
  if (!dateRegex.test(birthDate)) {
    return false
  }

  const date = new Date(birthDate)
  const today = new Date()
  
  // 유효한 날짜인지 확인
  if (isNaN(date.getTime())) {
    return false
  }

  // 미래 날짜는 불가
  if (date > today) {
    return false
  }

  return true
}

/**
 * 파일 크기 검증
 */
export const validateFileSize = (file: File, maxSize: number = 5 * 1024 * 1024): boolean => {
  return file.size <= maxSize
}

/**
 * 이미지 파일 형식 검증
 */
export const validateImageType = (file: File): boolean => {
  const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif']
  return allowedTypes.includes(file.type)
}


