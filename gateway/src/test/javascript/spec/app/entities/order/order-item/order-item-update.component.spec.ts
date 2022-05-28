/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import OrderItemUpdateComponent from '@/entities/order/order-item/order-item-update.vue';
import OrderItemClass from '@/entities/order/order-item/order-item-update.component';
import OrderItemService from '@/entities/order/order-item/order-item.service';

import OrderService from '@/entities/order/order/order.service';
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
  describe('OrderItem Management Update Component', () => {
    let wrapper: Wrapper<OrderItemClass>;
    let comp: OrderItemClass;
    let orderItemServiceStub: SinonStubbedInstance<OrderItemService>;

    beforeEach(() => {
      orderItemServiceStub = sinon.createStubInstance<OrderItemService>(OrderItemService);

      wrapper = shallowMount<OrderItemClass>(OrderItemUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          orderItemService: () => orderItemServiceStub,
          alertService: () => new AlertService(),

          orderService: () =>
            sinon.createStubInstance<OrderService>(OrderService, {
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
        comp.orderItem = entity;
        orderItemServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(orderItemServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.orderItem = entity;
        orderItemServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(orderItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundOrderItem = { id: 123 };
        orderItemServiceStub.find.resolves(foundOrderItem);
        orderItemServiceStub.retrieve.resolves([foundOrderItem]);

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
