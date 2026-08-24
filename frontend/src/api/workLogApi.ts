import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api'

export interface WorkLog {
  id: number
  todoId: number
  workDate: string
  hours: number
  createdAt: string
  updatedAt: string
}

export interface WorkLogRequest {
  workDate: string
  hours: number
}

// 作業記録一覧取得
export const getWorkLogs = async (
  todoId: number,
): Promise<WorkLog[]> => {
  const response = await axios.get<WorkLog[]>(
    `${API_BASE_URL}/todos/${todoId}/work-logs`,
  )

  return response.data
}

// 作業記録登録
export const createWorkLog = async (
  todoId: number,
  request: WorkLogRequest,
): Promise<WorkLog> => {
  const response = await axios.post<WorkLog>(
    `${API_BASE_URL}/todos/${todoId}/work-logs`,
    request,
  )

  return response.data
}

// 作業記録更新
export const updateWorkLog = async (
  todoId: number,
  id: number,
  request: WorkLogRequest,
): Promise<WorkLog> => {
  const response = await axios.put<WorkLog>(
    `${API_BASE_URL}/todos/${todoId}/work-logs/${id}`,
    request,
  )

  return response.data
}

// 作業記録削除
export const deleteWorkLog = async (
  todoId: number,
  id: number,
): Promise<void> => {
  await axios.delete(
    `${API_BASE_URL}/todos/${todoId}/work-logs/${id}`,
  )
}

// 実績工数取得
export const getActualHours = async (
  todoId: number,
): Promise<number> => {
  const response = await axios.get<number>(
    `${API_BASE_URL}/todos/${todoId}/work-logs/actual-hours`,
  )

  return response.data
}
