import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080/api/projects'

//expotとはこのファイルの外からも使えるようにする
export interface Project { //バックエンドのOrojectResponseに対応している
  id: number
  name: string
  description: string
  goal: string
  motivation: string
  status: string
  priority: string
  startDate: string
  plannedEndDate: string
  actualEndDate: string | null
  plannedHours: number
  createdAt: string
  updatedAt: string
}

export interface ProjectCreateRequest { //バックエンドのProjectCreateRequestに対応している
  name: string
  description: string
  goal: string
  motivation: string
  status: string
  priority: string
  startDate: string
  plannedEndDate: string
  plannedHours: number
}

// プロジェクトの一覧を取得する関数
//exportを使って、他のファイルからgetProjects関数を呼び出せるようにする
export const getProjects = async (): Promise<Project[]> => {
  const response = await axios.get<Project[]>(API_BASE_URL)
  return response.data
}

// 指定されたIDのプロジェクトを取得する関数
export const getProject = async (id: number): Promise<Project> => {
  const response = await axios.get<Project>(`${API_BASE_URL}/${id}`)
  return response.data
}

// 新しいプロジェクトを作成する関数
export const createProject = async (
  project: ProjectCreateRequest,
): Promise<Project> => {
  const response = await axios.post<Project>(API_BASE_URL, project)
  return response.data
}
