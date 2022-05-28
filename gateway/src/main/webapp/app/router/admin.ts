import { Authority } from '@/shared/security/authority';

const AuUserManagementComponent = () => import('@/admin/user-management/user-management.vue');
const AuUserManagementViewComponent = () => import('@/admin/user-management/user-management-view.vue');
const AuUserManagementEditComponent = () => import('@/admin/user-management/user-management-edit.vue');
const AuDocsComponent = () => import('@/admin/docs/docs.vue');
const AuConfigurationComponent = () => import('@/admin/configuration/configuration.vue');
const AuHealthComponent = () => import('@/admin/health/health.vue');
const AuLogsComponent = () => import('@/admin/logs/logs.vue');
const AuMetricsComponent = () => import('@/admin/metrics/metrics.vue');
const AuGatewayComponent = () => import('@/admin/gateway/gateway.vue');

export default [
  {
    path: '/admin/user-management',
    name: 'AuUser',
    component: AuUserManagementComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/user-management/new',
    name: 'AuUserCreate',
    component: AuUserManagementEditComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/user-management/:userId/edit',
    name: 'AuUserEdit',
    component: AuUserManagementEditComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/user-management/:userId/view',
    name: 'AuUserView',
    component: AuUserManagementViewComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/docs',
    name: 'AuDocsComponent',
    component: AuDocsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/health',
    name: 'AuHealthComponent',
    component: AuHealthComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/logs',
    name: 'AuLogsComponent',
    component: AuLogsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/metrics',
    name: 'AuMetricsComponent',
    component: AuMetricsComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/configuration',
    name: 'AuConfigurationComponent',
    component: AuConfigurationComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
  {
    path: '/admin/gateway',
    name: 'AuGatewayComponent',
    component: AuGatewayComponent,
    meta: { authorities: [Authority.ADMIN] },
  },
];
