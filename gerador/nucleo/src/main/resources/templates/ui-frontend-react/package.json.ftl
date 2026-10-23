{
  "name": "${nome}",
  "version": "${versao}",
  "private": true,
  "type": "module",
  "packageManager": "npm@11.12.1",
  "scripts": {
    "start": "vite",
    "build": "tsc -b && vite build",
    "preview": "vite preview",
    "test": "vitest run",
    "test:watch": "vitest"
  },
  "dependencies": {
    "@tanstack/react-query": "^5.104.0",
    "bootstrap": "^5.3.8",
    "react": "^19.3.0",
    "react-bootstrap": "^2.10.10",
    "react-dom": "^19.3.0",
    "react-hook-form": "^7.90.0",
    "react-router": "^8.5.0"
  },
  "devDependencies": {
    "@testing-library/jest-dom": "^7.0.0",
    "@testing-library/react": "^16.3.0",
    "@testing-library/user-event": "^14.6.0",
    "@types/node": "^24.0.0",
    "@types/react": "^19.3.0",
    "@types/react-dom": "^19.3.0",
    "@vitejs/plugin-react": "^6.1.0",
    "jsdom": "^30.0.0",
    "prettier": "^3.8.1",
    "typescript": "~6.0.2",
    "vite": "^8.3.0",
    "vitest": "^5.0.0"
  }
}
