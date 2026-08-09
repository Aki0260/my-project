# API設計・完全仕様書（実装確定版）

## 1. API設計の位置づけ

このアプリでは、Vue側の画面とSpring Boot側のバックエンドをREST APIで接続する。

APIは機能ごとに責務を分け、DBに保存するデータとバックエンドで計算するデータを整理する。

## 2. 全体構造

```
                    ┌──────────────┐
                    │  Dashboard   │
                    └──────┬───────┘
                           │ 集計
                           ↓
┌──────────┐       ┌──────────┐
│ Project  │──────→│   WBS    │
└──────────┘       └────┬─────┘
                         │
                         ↓
                    ┌─────────┐
                    │  ToDo   │
                    └────┬────┘
                         │
                         ↓
                   ┌───────────┐
                   │ Work Logs │
                   └───────────┘

                   ┌───────────────┐
                   │ Work Settings │
                   └───────┬───────┘
                           │
                           ↓
                      余日差計算
```

## 3. 全API一覧

**MVPの確定API：28エンドポイント**

### Project API：5

| Method | Endpoint | 用途 |
| --- | --- | --- |
| GET | `/api/projects` | プロジェクト一覧 |
| GET | `/api/projects/{id}` | プロジェクト詳細 |
| POST | `/api/projects` | プロジェクト作成 |
| PUT | `/api/projects/{id}` | プロジェクト更新 |
| DELETE | `/api/projects/{id}` | プロジェクト削除 |

### WBS API：5

| Method | Endpoint | 用途 |
| --- | --- | --- |
| GET | `/api/projects/{projectId}/wbs` | プロジェクトのWBS一覧 |
| GET | `/api/wbs/{id}` | WBS詳細 |
| POST | `/api/projects/{projectId}/wbs` | WBS作成 |
| PUT | `/api/wbs/{id}` | WBS更新 |
| DELETE | `/api/wbs/{id}` | WBS削除 |

### ToDo API：6

| Method | Endpoint | 用途 |
| --- | --- | --- |
| GET | `/api/wbs/{wbsId}/todos` | WBSのToDo一覧 |
| GET | `/api/projects/{projectId}/todos` | プロジェクト内ToDo一覧 |
| GET | `/api/todos/{id}` | ToDo詳細 |
| POST | `/api/wbs/{wbsId}/todos` | ToDo作成 |
| PUT | `/api/todos/{id}` | ToDo更新 |
| DELETE | `/api/todos/{id}` | ToDo削除 |

### Work Logs API：5

| Method | Endpoint | 用途 |
| --- | --- | --- |
| GET | `/api/todos/{todoId}/work-logs` | ToDoの作業記録一覧 |
| GET | `/api/work-logs/{id}` | 作業記録詳細 |
| POST | `/api/todos/{todoId}/work-logs` | 作業記録追加 |
| PUT | `/api/work-logs/{id}` | 作業記録更新 |
| DELETE | `/api/work-logs/{id}` | 作業記録削除 |

### Work Settings API：6

| Method | Endpoint | 用途 |
| --- | --- | --- |
| GET | `/api/work-settings` | 曜日別デフォルト取得 |
| PUT | `/api/work-settings` | 曜日別デフォルト更新 |
| GET | `/api/work-settings/daily` | 日別設定一覧 |
| POST | `/api/work-settings/daily` | 日別設定追加 |
| PUT | `/api/work-settings/daily/{date}` | 日別設定変更 |
| DELETE | `/api/work-settings/daily/{date}` | 日別設定削除・デフォルト復帰 |

### Dashboard API：1

| Method | Endpoint | 用途 |
| --- | --- | --- |
| GET | `/api/dashboard` | ダッシュボード情報取得 |

---

## 4. Project API 詳細仕様

### 一覧取得：GET `/api/projects`

一覧画面に必要な情報を返す。詳細画面専用の説明・意気込み・工数詳細などは一覧レスポンスには含めない方針。

**レスポンス例：**

```json
[
  {
    "id": 1,
    "name": "個人開発アプリ",
    "goal": "8月中にMVP完成",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "startDate": "2026-08-05",
    "plannedEndDate": "2026-08-23",
    "progress": 65
  }
]
```

### 詳細取得：GET `/api/projects/{id}`

詳細画面に必要な情報に加え、実績工数・残工数・進捗率などの計算値もバックエンドで算出して返す。

**レスポンス例：**

```json
{
  "id": 1,
  "name": "個人開発アプリ",
  "description": "個人向けプロジェクト管理ツール",
  "goal": "8月中にMVP完成",
  "motivation": "設計からちゃんと考えて作る",
  "status": "IN_PROGRESS",
  "priority": "HIGH",
  "startDate": "2026-08-05",
  "plannedEndDate": "2026-08-23",
  "actualEndDate": null,
  "plannedHours": 50,
  "actualHours": 32,
  "remainingHours": 18,
  "progress": 65
}
```

### 作成：POST `/api/projects`

必須項目：`name`, `goal`, `motivation`, `plannedHours`

**リクエスト例：**

```json
{
  "name": "個人開発アプリ",
  "description": "個人向けプロジェクト管理ツール",
  "goal": "8月中にMVP完成",
  "motivation": "設計からちゃんと考えて作る",
  "status": "NOT_STARTED",
  "priority": "HIGH",
  "startDate": "2026-08-05",
  "plannedEndDate": "2026-08-23",
  "plannedHours": 50
}
```

### 更新：PUT `/api/projects/{id}`

作成APIとほぼ同じ形式でプロジェクト情報を更新する。

### 削除：DELETE `/api/projects/{id}`

成功時は `204 No Content` を返す。

### Project API 設計方針

- フロントエンドとSpring Boot間の責務を明確にする。
- 計算値は基本的にバックエンド側で算出して返す。
- 一覧と詳細で返す情報を分ける。

---

## 5. WBS API 詳細仕様

### 一覧取得：GET `/api/projects/{projectId}/wbs`

親子関係を表示できるよう `parentId` を返す。予定工数・実績工数・進捗率も返す。

### 詳細取得：GET `/api/wbs/{id}`

WBS詳細に必要な情報に加えて、実績工数・残工数・進捗率などの計算値も返す。

### 作成：POST `/api/projects/{projectId}/wbs`

**リクエスト例：**

```json
{
  "name": "API設計",
  "parentId": 1,
  "status": "NOT_STARTED",
  "startDate": "2026-08-07",
  "plannedEndDate": "2026-08-07",
  "plannedHours": 1
}
```

`parentId` が `null` の場合はプロジェクト直下のWBSとする。

### 更新：PUT `/api/wbs/{id}`

作成時とほぼ同じ形式でWBS情報を更新する。

### 削除：DELETE `/api/wbs/{id}`

成功時は `204 No Content`。

**削除ルール：WBSを削除した場合、その配下にある子WBSも再帰的にまとめて削除する。**

### WBS API 設計方針

- WBSはプロジェクトに所属する。
- `parentId` により親子構造を管理する。
- 計算値は基本的にバックエンド側で算出して返す。

---

## 6. ToDo API 詳細仕様

### WBSのToDo一覧：GET `/api/wbs/{wbsId}/todos`

WBSに所属するToDoを取得する。WBS画面から関連ToDoを表示するために使用する。

### プロジェクト内ToDo一覧：GET `/api/projects/{projectId}/todos`

プロジェクト内のToDoをWBSをまたいで横断取得する。レスポンスには `wbsName` も含め、どのWBSに属するToDoか確認できるようにする。

### ToDo詳細：GET `/api/todos/{id}`

ToDoの基本情報に加えて、WBS名・予定工数・実績工数・残工数などを返す。

### 作成：POST `/api/wbs/{wbsId}/todos`

**リクエスト例：**

```json
{
  "name": "API一覧作成",
  "status": "NOT_STARTED",
  "priority": "HIGH",
  "dueDate": "2026-08-07",
  "plannedHours": 1,
  "description": "APIのエンドポイントを整理する"
}
```

必須項目：タスク名、WBS、ステータス、優先度。WBSはURLの `wbsId` で指定する。

任意項目：期限、予定工数、説明。

### 更新：PUT `/api/todos/{id}`

作成時とほぼ同じ形式で更新する。**`wbsId` も更新可能とし、作成後にToDoを別WBSへ移動できるようにする。**

### 削除：DELETE `/api/todos/{id}`

成功時は `204 No Content`。ToDoには子要素がないため、WBSのような連鎖削除は行わない。

### ToDo API 設計方針

- ToDoはWBSに所属する。
- プロジェクト詳細からプロジェクト内のToDoを横断確認できる。
- 計算値は基本的にバックエンド側で算出して返す。
- ToDoの所属WBSは後から変更可能とする。

---

## 7. Work Logs API 詳細仕様

### 作業記録の基本方針

- 工数の基準単位は「時間」。
- 入力は **0.5時間（30分）単位** とする。
- 作業記録は「作業日＋工数＋メモ」で管理する。
- 1つのToDoに複数の作業記録を登録できる。
- 作業記録の合計をToDoの実績工数とする。
- 実績工数はWBS・プロジェクトにも集計する。

### 一覧取得：GET `/api/todos/{todoId}/work-logs`

**レスポンス例：**

```json
[
  {
    "id": 1,
    "todoId": 10,
    "workDate": "2026-08-10",
    "hours": 1.0,
    "memo": "API仕様を整理"
  },
  {
    "id": 2,
    "todoId": 10,
    "workDate": "2026-08-11",
    "hours": 0.5,
    "memo": "レスポンス設計"
  }
]
```

### 詳細取得：GET `/api/work-logs/{id}`

作業日、工数、メモ、ToDoとの紐付けを返す。

### 追加：POST `/api/todos/{todoId}/work-logs`

**リクエスト例：**

```json
{
  "workDate": "2026-08-10",
  "hours": 1.5,
  "memo": "API仕様を整理"
}
```

`hours` は0.5時間単位で入力する。

### 更新：PUT `/api/work-logs/{id}`

作業日、工数、メモを更新できる。

### 削除：DELETE `/api/work-logs/{id}`

成功時は `204 No Content`。削除後はToDoの実績工数を再集計する。

### 工数集計

作業記録を積み上げて、以下を算出する。

- ToDoの実績工数
- WBSの実績工数
- プロジェクトの実績工数
- 残工数
- 進捗率

---

## 8. Work Settings API 詳細仕様

### 基本設定

曜日ごとのデフォルト作業可能時間を管理する。

**レスポンス例：**

```json
{
  "monday": 2.0,
  "tuesday": 2.0,
  "wednesday": 2.0,
  "thursday": 2.0,
  "friday": 2.0,
  "saturday": 4.0,
  "sunday": 0.0
}
```

### 日別設定

予定や休暇などにより、特定日の作業可能時間を個別に上書きできる。

**レスポンス例：**

```json
[
  {
    "date": "2026-08-11",
    "availableHours": 1.0
  },
  {
    "date": "2026-08-13",
    "availableHours": 0.0
  }
]
```

### 日別設定の削除

日別設定を削除した場合、その日は曜日別のデフォルト値に戻る。

### 作業可能時間の決定ルール

1. 対象日に日別設定が存在する場合、その値を使用する。
2. 日別設定がない場合、対象曜日のデフォルト値を使用する。

### 余日差への利用

期限までの日ごとの作業可能時間を算出し、残工数と比較することで余日差を算出する。

例：

```
残工数：10h
期限までの作業可能時間：11h
→ 1h分の余裕
```

### Work Settings 設計方針

- 工数の基準単位は時間。
- 曜日別デフォルト設定を持つ。
- 特定日の予定・休暇などは日別設定で上書きする。
- 日別設定がなければ曜日別デフォルトを利用する。

---

## 9. Dashboard API 詳細仕様

### レスポンス内容

ダッシュボード専用のデータをDBに保存せず、既存のプロジェクト・WBS・ToDo・Work Logs・Work Settingsをバックエンドで集計して返す。

**レスポンス例：**

```json
{
  "summary": {
    "totalProjects": 3,
    "inProgressProjects": 2,
    "completedProjects": 1
  },
  "projects": [
    {
      "projectId": 1,
      "projectName": "個人開発アプリ",
      "status": "IN_PROGRESS",
      "progress": 65,
      "plannedHours": 50,
      "actualHours": 32,
      "remainingHours": 18,
      "remainingDays": 7,
      "availableHours": 20,
      "scheduleDifferenceHours": 2,
      "scheduleDifferenceDays": 1
    }
  ],
  "risks": [
    {
      "projectId": 2,
      "projectName": "資格勉強",
      "scheduleDifferenceHours": -3,
      "scheduleDifferenceDays": -1.5,
      "riskLevel": "HIGH"
    }
  ]
}
```

### ダッシュボード表示内容

- プロジェクト概要（全体・進行中・完了）
- プロジェクトごとの進捗率
- 予定工数・実績工数・残工数
- 期限までの残り日数
- 期限までの作業可能時間
- 余日差（時間）
- 余日差（日数）※将来的な日数表示に利用
- 遅延リスクのあるプロジェクト

### 余日差・リスク判定

基本的には余日差を基準として、余裕あり・注意・遅延の3段階で判定する。具体的なしきい値は実装時に調整可能とする。

### 集計ロジック

```
ToDoの実績工数
    ↓ 合計
WBSの実績工数
    ↓ 合計
プロジェクトの実績工数

Work Settings
    ↓
期限までの作業可能時間
    ↓
残工数と比較
    ↓
余日差を算出
```

### Dashboard 設計方針

- ダッシュボード専用データはDBに保存しない。
- 集計・計算はバックエンド側で行う。
- 時間単位を基本表示とし、将来的に日数単位でも確認できるようにする。

---

## 10. 重要な設計ルール

### プロジェクト

- 一覧と詳細で返す情報を分ける。
- 実績工数・残工数・進捗率などの計算値はバックエンド側で算出する。

### WBS

- `parentId` で親子構造を管理する。
- `parentId = null` はプロジェクト直下。
- 親WBSを削除すると、配下の子WBSも再帰的に削除する。

### ToDo

- ToDoはWBSに所属する。
- プロジェクト単位で横断取得できる。
- 作成後でも `wbsId` を変更でき、別WBSへ移動可能。

### Work Logs

- 工数の基準単位は時間。
- 入力は0.5時間（30分）単位。
- 作業記録は「作業日＋工数＋メモ」。
- 1ToDoに複数記録を登録できる。
- ToDo → WBS → プロジェクトへ実績工数を集計する。

### Work Settings

- 曜日別のデフォルト作業可能時間を持つ。
- 特定日は個別設定で上書きできる。
- 日別設定がなければ曜日別デフォルトを使用する。
- 日別設定を削除するとデフォルトへ戻る。

### Dashboard

- ダッシュボード専用データはDBに保存しない。
- 既存データをバックエンドで集計して返す。
- 進捗、工数、残り日数、作業可能時間、余日差、遅延リスクを表示する。

---

## 11. 余日差の計算イメージ

```
ToDoの実績工数
      ↓ 合計
WBSの実績工数
      ↓ 合計
プロジェクトの実績工数

Work Settings
      ↓
期限までの日ごとの作業可能時間を算出
      ↓
期限までの作業可能時間 − 残工数
      ↓
余日差を算出
```

例：

```
残工数：10h
期限までの作業可能時間：11h
→ +1hの余裕
```

---

## 12. API設計時の責務分担

- Vue：画面表示、入力、ユーザー操作
- Spring Boot Controller：HTTPリクエスト受付・レスポンス返却
- Service：業務ロジック、工数集計、余日差計算など
- Repository：DBアクセス
- DB：永続化する元データを保持

---

## 13. 実装フェーズへの引き継ぎ

API設計はここまでをMVPの確定仕様とする。

次の工程ではSpring Boot側でController / Service / Repository / DTOを実装し、各APIを順番に動作確認する。

実装順は基本的に、

1. Project
2. WBS
3. ToDo
4. Work Logs
5. Work Settings
6. Dashboard

の順とする。