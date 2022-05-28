/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import CusAccountDetailComponent from '@/entities/customer/cus-account/cus-account-details.vue';
import CusAccountClass from '@/entities/customer/cus-account/cus-account-details.component';
import CusAccountService from '@/entities/customer/cus-account/cus-account.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('CusAccount Management Detail Component', () => {
    let wrapper: Wrapper<CusAccountClass>;
    let comp: CusAccountClass;
    let cusAccountServiceStub: SinonStubbedInstance<CusAccountService>;

    beforeEach(() => {
      cusAccountServiceStub = sinon.createStubInstance<CusAccountService>(CusAccountService);

      wrapper = shallowMount<CusAccountClass>(CusAccountDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { cusAccountService: () => cusAccountServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundCusAccount = { id: 123 };
        cusAccountServiceStub.find.resolves(foundCusAccount);

        // WHEN
        comp.retrieveCusAccount(123);
        await comp.$nextTick();

        // THEN
        expect(comp.cusAccount).toBe(foundCusAccount);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundCusAccount = { id: 123 };
        cusAccountServiceStub.find.resolves(foundCusAccount);

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
