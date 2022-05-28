/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CusAccountComponent from '@/entities/customer/cus-account/cus-account.vue';
import CusAccountClass from '@/entities/customer/cus-account/cus-account.component';
import CusAccountService from '@/entities/customer/cus-account/cus-account.service';
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
  describe('CusAccount Management Component', () => {
    let wrapper: Wrapper<CusAccountClass>;
    let comp: CusAccountClass;
    let cusAccountServiceStub: SinonStubbedInstance<CusAccountService>;

    beforeEach(() => {
      cusAccountServiceStub = sinon.createStubInstance<CusAccountService>(CusAccountService);
      cusAccountServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<CusAccountClass>(CusAccountComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          cusAccountService: () => cusAccountServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      cusAccountServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllCusAccounts();
      await comp.$nextTick();

      // THEN
      expect(cusAccountServiceStub.retrieve.called).toBeTruthy();
      expect(comp.cusAccounts[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      cusAccountServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(cusAccountServiceStub.retrieve.callCount).toEqual(1);

      comp.removeCusAccount();
      await comp.$nextTick();

      // THEN
      expect(cusAccountServiceStub.delete.called).toBeTruthy();
      expect(cusAccountServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
