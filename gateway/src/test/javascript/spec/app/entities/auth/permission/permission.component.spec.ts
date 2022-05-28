/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import PermissionComponent from '@/entities/auth/permission/permission.vue';
import PermissionClass from '@/entities/auth/permission/permission.component';
import PermissionService from '@/entities/auth/permission/permission.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(ToastPlugin);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Permission Management Component', () => {
    let wrapper: Wrapper<PermissionClass>;
    let comp: PermissionClass;
    let permissionServiceStub: SinonStubbedInstance<PermissionService>;

    beforeEach(() => {
      permissionServiceStub = sinon.createStubInstance<PermissionService>(PermissionService);
      permissionServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<PermissionClass>(PermissionComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          permissionService: () => permissionServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      permissionServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllPermissions();
      await comp.$nextTick();

      // THEN
      expect(permissionServiceStub.retrieve.called).toBeTruthy();
      expect(comp.permissions[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      permissionServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(permissionServiceStub.retrieve.callCount).toEqual(1);

      comp.removePermission();
      await comp.$nextTick();

      // THEN
      expect(permissionServiceStub.delete.called).toBeTruthy();
      expect(permissionServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
