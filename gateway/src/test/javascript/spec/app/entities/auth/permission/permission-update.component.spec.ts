/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PermissionUpdateComponent from '@/entities/auth/permission/permission-update.vue';
import PermissionClass from '@/entities/auth/permission/permission-update.component';
import PermissionService from '@/entities/auth/permission/permission.service';

import RoleService from '@/entities/auth/role/role.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Permission Management Update Component', () => {
    let wrapper: Wrapper<PermissionClass>;
    let comp: PermissionClass;
    let permissionServiceStub: SinonStubbedInstance<PermissionService>;

    beforeEach(() => {
      permissionServiceStub = sinon.createStubInstance<PermissionService>(PermissionService);

      wrapper = shallowMount<PermissionClass>(PermissionUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          permissionService: () => permissionServiceStub,
          alertService: () => new AlertService(),

          roleService: () =>
            sinon.createStubInstance<RoleService>(RoleService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.permission = entity;
        permissionServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(permissionServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.permission = entity;
        permissionServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(permissionServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundPermission = { id: 123 };
        permissionServiceStub.find.resolves(foundPermission);
        permissionServiceStub.retrieve.resolves([foundPermission]);

        // WHEN
        comp.beforeRouteEnter({ params: { permissionId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.permission).toBe(foundPermission);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
