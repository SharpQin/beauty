/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import CusAccountUpdateComponent from '@/entities/customer/cus-account/cus-account-update.vue';
import CusAccountClass from '@/entities/customer/cus-account/cus-account-update.component';
import CusAccountService from '@/entities/customer/cus-account/cus-account.service';

import CustomerService from '@/entities/customer/customer/customer.service';
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
  describe('CusAccount Management Update Component', () => {
    let wrapper: Wrapper<CusAccountClass>;
    let comp: CusAccountClass;
    let cusAccountServiceStub: SinonStubbedInstance<CusAccountService>;

    beforeEach(() => {
      cusAccountServiceStub = sinon.createStubInstance<CusAccountService>(CusAccountService);

      wrapper = shallowMount<CusAccountClass>(CusAccountUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          cusAccountService: () => cusAccountServiceStub,
          alertService: () => new AlertService(),

          customerService: () =>
            sinon.createStubInstance<CustomerService>(CustomerService, {
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
        comp.cusAccount = entity;
        cusAccountServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cusAccountServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.cusAccount = entity;
        cusAccountServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(cusAccountServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCusAccount = { id: 123 };
        cusAccountServiceStub.find.resolves(foundCusAccount);
        cusAccountServiceStub.retrieve.resolves([foundCusAccount]);

        // WHEN
        comp.beforeRouteEnter({ params: { cusAccountId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.cusAccount).toBe(foundCusAccount);
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
