/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import OrderItemDetailComponent from '@/entities/order/order-item/order-item-details.vue';
import OrderItemClass from '@/entities/order/order-item/order-item-details.component';
import OrderItemService from '@/entities/order/order-item/order-item.service';
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
  describe('OrderItem Management Detail Component', () => {
    let wrapper: Wrapper<OrderItemClass>;
    let comp: OrderItemClass;
    let orderItemServiceStub: SinonStubbedInstance<OrderItemService>;

    beforeEach(() => {
      orderItemServiceStub = sinon.createStubInstance<OrderItemService>(OrderItemService);

      wrapper = shallowMount<OrderItemClass>(OrderItemDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { orderItemService: () => orderItemServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundOrderItem = { id: 123 };
        orderItemServiceStub.find.resolves(foundOrderItem);

        // WHEN
        comp.retrieveOrderItem(123);
        await comp.$nextTick();

        // THEN
        expect(comp.orderItem).toBe(foundOrderItem);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundOrderItem = { id: 123 };
        orderItemServiceStub.find.resolves(foundOrderItem);

        // WHEN
        comp.beforeRouteEnter({ params: { orderItemId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.orderItem).toBe(foundOrderItem);
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
