/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProductItemUpdateComponent from '@/entities/product/product-item/product-item-update.vue';
import ProductItemClass from '@/entities/product/product-item/product-item-update.component';
import ProductItemService from '@/entities/product/product-item/product-item.service';

import ProductService from '@/entities/product/product/product.service';
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
  describe('ProductItem Management Update Component', () => {
    let wrapper: Wrapper<ProductItemClass>;
    let comp: ProductItemClass;
    let productItemServiceStub: SinonStubbedInstance<ProductItemService>;

    beforeEach(() => {
      productItemServiceStub = sinon.createStubInstance<ProductItemService>(ProductItemService);

      wrapper = shallowMount<ProductItemClass>(ProductItemUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          productItemService: () => productItemServiceStub,
          alertService: () => new AlertService(),

          productService: () =>
            sinon.createStubInstance<ProductService>(ProductService, {
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
        comp.productItem = entity;
        productItemServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productItemServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.productItem = entity;
        productItemServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productItemServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProductItem = { id: 123 };
        productItemServiceStub.find.resolves(foundProductItem);
        productItemServiceStub.retrieve.resolves([foundProductItem]);

        // WHEN
        comp.beforeRouteEnter({ params: { productItemId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.productItem).toBe(foundProductItem);
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
