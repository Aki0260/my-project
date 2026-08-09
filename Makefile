.PHONY: lint lint-frontend lint-backend pre-pr

# フロントエンド Lint
lint-frontend:
	cd frontend && npm run lint

# バックエンド Lint
lint-backend:
	cd backend && ./mvnw checkstyle:check

# 両方実行
lint: lint-frontend lint-backend
	@echo "✅ All linting completed!"

# PR 前の完全チェック
pre-pr: lint
	@echo "✅ Ready for PR!"