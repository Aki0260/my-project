# データベース スキーマ定義

## 概要

プロジェクト管理ツールのMVP実装に必要なテーブル定義。

工数の基準単位は「時間」。日付はISO 8601形式（YYYY-MM-DD）。

---

## テーブル一覧

1. `projects` - プロジェクト
2. `wbs` - Work Breakdown Structure
3. `todos` - タスク
4. `work_logs` - 作業記録
5. `work_settings_default` - 曜日別作業可能時間
6. `work_settings_daily` - 日別作業可能時間

---

## 1. projects テーブル

プロジェクト基本情報を保持。

```sql
CREATE TABLE projects (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL COMMENT 'プロジェクト名',
  description TEXT COMMENT '説明',
  goal VARCHAR(255) NOT NULL COMMENT '目標',
  motivation TEXT NOT NULL COMMENT '意気込み',
  status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED' COMMENT 'ステータス：NOT_STARTED, IN_PROGRESS, COMPLETED, ON_HOLD',
  priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM' COMMENT '優先度：LOW, MEDIUM, HIGH',
  start_date DATE COMMENT '開始日',
  planned_end_date DATE COMMENT '終了予定日',
  actual_end_date DATE COMMENT '実際の終了日',
  planned_hours DECIMAL(10, 1) NOT NULL COMMENT '予定工数（時間）',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_status (status),
  INDEX idx_priority (priority),
  INDEX idx_start_date (start_date),
  INDEX idx_planned_end_date (planned_end_date)
);
```

### カラム説明

- `id`: プロジェクトの一意識別子
- `name`: プロジェクト名（必須）
- `description`: 説明文（オプション）
- `goal`: 達成目標（必須）
- `motivation`: 実施意気込み（必須）
- `status`: プロジェクト状態
  - `NOT_STARTED`: 未着手
  - `IN_PROGRESS`: 進行中
  - `COMPLETED`: 完了
  - `ON_HOLD`: 保留
- `priority`: 優先度（必須）
  - `LOW`: 低
  - `MEDIUM`: 中
  - `HIGH`: 高
- `start_date`: プロジェクト開始日（オプション）
- `planned_end_date`: 予定終了日（オプション）
- `actual_end_date`: 実際の終了日（ステータスがCOMPLETEDの時に設定）
- `planned_hours`: 予定工数（時間単位、1小数点まで）
- `created_at`: 作成日時
- `updated_at`: 更新日時

---

## 2. wbs テーブル

Work Breakdown Structureを表現。親子関係で階層構造を持つ。

```sql
CREATE TABLE wbs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  project_id BIGINT NOT NULL COMMENT 'プロジェクトID',
  parent_id BIGINT COMMENT '親WBS ID（NULL = プロジェクト直下）',
  name VARCHAR(255) NOT NULL COMMENT 'WBS名',
  status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED' COMMENT 'ステータス：NOT_STARTED, IN_PROGRESS, COMPLETED',
  start_date DATE COMMENT '開始日',
  planned_end_date DATE COMMENT '終了予定日',
  planned_hours DECIMAL(10, 1) COMMENT '予定工数（時間）',
  sort_order INT DEFAULT 0 COMMENT '表示順序',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  FOREIGN KEY (parent_id) REFERENCES wbs(id) ON DELETE CASCADE,
  INDEX idx_project_id (project_id),
  INDEX idx_parent_id (parent_id),
  INDEX idx_status (status),
  INDEX idx_sort_order (sort_order)
);
```

### カラム説明

- `id`: WBSの一意識別子
- `project_id`: 所属するプロジェクトID（必須）
- `parent_id`: 親WBSのID（NULL の場合はプロジェクト直下）
- `name`: WBS名（必須）
- `status`: WBS状態
  - `NOT_STARTED`: 未着手
  - `IN_PROGRESS`: 進行中
  - `COMPLETED`: 完了
- `start_date`: WBS開始日（オプション）
- `planned_end_date`: WBS終了予定日（オプション）
- `planned_hours`: 予定工数（時間単位、オプション）
- `sort_order`: 表示順序（同一階層内での並び順）
- `created_at`: 作成日時
- `updated_at`: 更新日時

### 構造例

```
Project: 個人開発アプリ
├── WBS1: 企画・設計（parent_id = NULL）
│   ├── WBS1-1: 仕様書作成（parent_id = WBS1.id）
│   └── WBS1-2: DB設計（parent_id = WBS1.id）
├── WBS2: 実装（parent_id = NULL）
│   ├── WBS2-1: バックエンド（parent_id = WBS2.id）
│   └── WBS2-2: フロントエンド（parent_id = WBS2.id）
└── WBS3: テスト・デプロイ（parent_id = NULL）
```

---

## 3. todos テーブル

具体的なタスク・ToDoを管理。WBSに所属する。

```sql
CREATE TABLE todos (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  wbs_id BIGINT NOT NULL COMMENT 'WBS ID',
  project_id BIGINT NOT NULL COMMENT 'プロジェクトID（高速検索用）',
  name VARCHAR(255) NOT NULL COMMENT 'タスク名',
  description TEXT COMMENT '説明',
  status VARCHAR(50) NOT NULL DEFAULT 'NOT_STARTED' COMMENT 'ステータス：NOT_STARTED, IN_PROGRESS, COMPLETED',
  priority VARCHAR(50) NOT NULL DEFAULT 'MEDIUM' COMMENT '優先度：LOW, MEDIUM, HIGH',
  due_date DATE COMMENT '期限',
  planned_hours DECIMAL(10, 1) COMMENT '予定工数（時間）',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (wbs_id) REFERENCES wbs(id) ON DELETE CASCADE,
  FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
  INDEX idx_wbs_id (wbs_id),
  INDEX idx_project_id (project_id),
  INDEX idx_status (status),
  INDEX idx_priority (priority),
  INDEX idx_due_date (due_date)
);
```

### カラム説明

- `id`: ToDoの一意識別子
- `wbs_id`: 所属するWBS ID（必須）
- `project_id`: プロジェクトID（高速な横断検索のため保持）
- `name`: タスク名（必須）
- `description`: 説明文（オプション）
- `status`: タスク状態（必須）
  - `NOT_STARTED`: 未着手
  - `IN_PROGRESS`: 進行中
  - `COMPLETED`: 完了
- `priority`: 優先度（必須）
  - `LOW`: 低
  - `MEDIUM`: 中
  - `HIGH`: 高
- `due_date`: 期限（オプション）
- `planned_hours`: 予定工数（時間単位、オプション）
- `created_at`: 作成日時
- `updated_at`: 更新日時

### 注記

- ToDoには開始日を持たせない。期限を中心に管理する設計。
- `project_id` を保持することで、プロジェクト内のToDo横断検索が高速化される。

---

## 4. work_logs テーブル

ToDoの実績工数を記録。複数の作業記録を積み上げてToDoの実績工数を算出。

```sql
CREATE TABLE work_logs (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  todo_id BIGINT NOT NULL COMMENT 'ToDo ID',
  work_date DATE NOT NULL COMMENT '作業日',
  hours DECIMAL(10, 1) NOT NULL COMMENT '作業時間（0.5時間単位）',
  memo TEXT COMMENT 'メモ',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (todo_id) REFERENCES todos(id) ON DELETE CASCADE,
  INDEX idx_todo_id (todo_id),
  INDEX idx_work_date (work_date),
  UNIQUE KEY unique_todo_date (todo_id, work_date) COMMENT '同一ToDoの同一日付には1レコード（集計用）'
);
```

### カラム説明

- `id`: 作業記録の一意識別子
- `todo_id`: 作業対象のToDo ID（必須）
- `work_date`: 作業日（必須、ISO 8601形式）
- `hours`: 作業時間（必須、0.5時間単位、例：0.5, 1.0, 1.5, 2.0）
- `memo`: 作業内容のメモ（オプション）
- `created_at`: 作成日時
- `updated_at`: 更新日時

### 注記

- `unique_todo_date` ユニークキーにより、同一ToDoの同一日付には最大1レコードという制約。
- 同日に複数の作業内容がある場合は、`hours` に合算した値を1レコードで記録。
- メモには複数の作業内容を列挙して記録。

### 集計ロジック

```
SELECT SUM(hours) FROM work_logs WHERE todo_id = ?
  → ToDoの実績工数

SELECT SUM(wl.hours) FROM work_logs wl
  JOIN todos t ON wl.todo_id = t.id
  WHERE t.wbs_id = ?
  → WBSの実績工数

SELECT SUM(wl.hours) FROM work_logs wl
  JOIN todos t ON wl.todo_id = t.id
  JOIN wbs w ON t.wbs_id = w.id
  WHERE w.project_id = ?
  → プロジェクトの実績工数
```

---

## 5. work_settings_default テーブル

曜日別の作業可能時間デフォルト値。

```sql
CREATE TABLE work_settings_default (
  id INT PRIMARY KEY AUTO_INCREMENT,
  day_of_week INT NOT NULL UNIQUE COMMENT '曜日（0=日, 1=月, ..., 6=土）',
  available_hours DECIMAL(10, 1) NOT NULL COMMENT '1日の作業可能時間',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_day_of_week (day_of_week)
);
```

### 初期値

```sql
INSERT INTO work_settings_default (day_of_week, available_hours) VALUES
  (0, 0.0),    -- 日曜日
  (1, 2.0),    -- 月曜日
  (2, 2.0),    -- 火曜日
  (3, 2.0),    -- 水曜日
  (4, 2.0),    -- 木曜日
  (5, 2.0),    -- 金曜日
  (6, 4.0);    -- 土曜日
```

### カラム説明

- `id`: 設定ID
- `day_of_week`: 曜日（0=日曜, 1=月曜, ..., 6=土曜）
- `available_hours`: 1日あたりの作業可能時間（時間単位、1小数点まで）
- `created_at`: 作成日時
- `updated_at`: 更新日時

### 用途

期限までの「実際に確保できる作業時間」を計算する際に使用。

---

## 6. work_settings_daily テーブル

特定日の作業可能時間を個別設定。予定や休暇などに対応。

```sql
CREATE TABLE work_settings_daily (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  date DATE NOT NULL UNIQUE COMMENT '日付',
  available_hours DECIMAL(10, 1) NOT NULL COMMENT '作業可能時間',
  reason VARCHAR(255) COMMENT '理由（休暇、予定など）',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  INDEX idx_date (date)
);
```

### カラム説明

- `id`: 設定ID
- `date`: 対象日（必須、一日一レコード）
- `available_hours`: 作業可能時間（必須）
  - `0.0` の場合は休日
  - `1.0` など通常と異なる値で予定対応
- `reason`: 設定理由（オプション、例："会議", "有給休暇", "移動日"）
- `created_at`: 作成日時
- `updated_at`: 更新日時

### 使用例

```
日付: 2026-08-11
available_hours: 1.0
reason: "午前打ち合わせ"

日付: 2026-08-13
available_hours: 0.0
reason: "有給休暇"
```

---

## 作業可能時間の決定ロジック

```
FUNCTION getAvailableHours(date: Date) {
  IF date が work_settings_daily に存在 {
    RETURN work_settings_daily.available_hours
  } ELSE {
    dayOfWeek = date.getDayOfWeek()
    RETURN work_settings_default[dayOfWeek].available_hours
  }
}
```

---

## 余日差の計算ロジック

```
FUNCTION calculateScheduleDifference(projectId: Long) {
  // 残工数を計算
  remainingHours = 
    project.planned_hours - SUM(work_logs.hours for project)
  
  // 期限までの日数を計算
  daysRemaining = 
    (project.planned_end_date - TODAY).days
  
  // 期限までの作業可能時間を計算
  availableHours = 0.0
  FOR each day from TODAY to project.planned_end_date {
    availableHours += getAvailableHours(day)
  }
  
  // 余日差を計算
  scheduleDifferenceHours = availableHours - remainingHours
  scheduleDifferenceDays = scheduleDifferenceHours / (availableHours / daysRemaining)
  
  RETURN {
    scheduleDifferenceHours,
    scheduleDifferenceDays,
    riskLevel: judgeRiskLevel(scheduleDifferenceHours)
  }
}

FUNCTION judgeRiskLevel(scheduleDifferenceHours: Double) {
  IF scheduleDifferenceHours >= 2.0 {
    RETURN "LOW"      // 🟢 余裕あり
  } ELSE IF scheduleDifferenceHours >= -1.0 {
    RETURN "MEDIUM"   // 🟡 ほぼ予定通り
  } ELSE {
    RETURN "HIGH"     // 🔴 遅延リスク
  }
}
```

---

## インデックス戦略

### projects テーブル
- `status`, `priority`: フィルタリング検索
- `start_date`, `planned_end_date`: 期限関連の検索

### wbs テーブル
- `project_id`: プロジェクト内のWBS検索
- `parent_id`: 親子関係の検索（ツリー構築）
- `status`: ステータスでのフィルタリング
- `sort_order`: 表示順序

### todos テーブル
- `wbs_id`: WBS内のToDo検索
- `project_id`: プロジェクト内のToDo横断検索（高速化）
- `status`, `priority`: フィルタリング
- `due_date`: 期限関連の検索

### work_logs テーブル
- `todo_id`: ToDo単位での集計
- `work_date`: 日別での検索

### work_settings_default テーブル
- `day_of_week`: 曜日別の検索

### work_settings_daily テーブル
- `date`: 日別の検索（期限までの作業可能時間算出）

---

## 設計原則

1. **工数の基準単位は時間**
   - すべての工数カラムは DECIMAL(10, 1) で時間単位を保持
   - 0.5時間（30分）単位での入力に対応

2. **計算値はDBに保存しない**
   - 実績工数・残工数・進捗率などの計算値はバックエンドで算出
   - 元データのみをDBに永続化

3. **集計性能を考慮**
   - `todos.project_id` など、横断検索を高速化するためのカラムを保持
   - 必要に応じて統計用テーブルの導入を検討

4. **削除時の連鎖動作**
   - プロジェクト削除 → WBS・ToDoも削除
   - WBS削除 → 子WBS・ToDoも削除
   - ToDo削除 → 作業記録も削除

5. **日付管理**
   - すべての日付は DATE型（ISO 8601形式）
   - タイムゾーン考慮不要な設計（個人用ツール）