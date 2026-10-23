import react from '@vitejs/plugin-react';
import { defineConfig } from 'vitest/config';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 4200,
    proxy: {
      '/backend': { target: 'http://localhost:8080', secure: false },
    },
  },
  test: {
    environment: 'jsdom',
    globals: true,
    setupFiles: ['src/configuracao-testes.ts'],
  },
});
