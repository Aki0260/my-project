import axios from 'axios'

// APIのベースURL
const API_BASE_URL = 'http://localhost:8080/api/todos'

// ToDoの型
export interface Todo {
  id: number
  projectId: number
  title: string
  plannedHours: number
  status: string
  dueDate: string
  memo: string
  createdAt: string
  updatedAt: string
}

// ToDo作成・更新用の型
export interface TodoRequest {
  projectId: number
  title: string
  plannedHours: number
  status: string
  dueDate: string
  memo: string
}

// ToDo一覧取得
export const getTodos = async (): Promise<Todo[]> => {
  const response = await axios.get<Todo[]>(API_BASE_URL)
  return response.data
}

// ToDo取得
export const getTodo = async (id: number): Promise<Todo> => {
  const response = await axios.get<Todo>(`${API_BASE_URL}/${id}`)
  return response.data
}

// ToDo作成
export const createTodo = async (
  todo: TodoRequest
): Promise<Todo> => {
  const response = await axios.post<Todo>(
    API_BASE_URL,
    todo
  )

  return response.data
}

// ToDo更新
export const updateTodo = async (
  id: number,
  todo: TodoRequest
): Promise<Todo> => {
  const response = await axios.put<Todo>(
    `${API_BASE_URL}/${id}`,
    todo
  )

  return response.data
}

// ToDo削除
export const deleteTodo = async (
  id: number
): Promise<void> => {
  await axios.delete(`${API_BASE_URL}/${id}`)
}
