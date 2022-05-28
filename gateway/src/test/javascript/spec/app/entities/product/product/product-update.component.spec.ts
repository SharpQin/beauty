/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import * as config from '@/shared/config/config';
import ProductUpdateComponent from '@/entities/product/product/product-update.vue';
import ProductClass from '@/entities/product/product/product-update.component';
import ProductService from '@/entities/product/product/product.service';

import ProductItemService from '@/entities/product/product-item/product-item.service';

import ProductCategoryService from '@/entities/product/product-category/product-category.service';
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
  describe('Product Management Update Component', () => {
    let wrapper: Wrapper<ProductClass>;
    let comp: ProductClass;
    let productServiceStub: SinonStubbedInstance<ProductService>;

    beforeEach(() => {
      productServiceStub = sinon.createStubInstance<ProductService>(ProductService);

      wrapper = shallowMount<ProductClass>(ProductUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          productService: () => productServiceStub,
          alertService: () => new AlertService(),

          productItemService: () =>
            sinon.createStubInstance<ProductItemService>(ProductItemService, {
              retrieve: sinon.stub().resolves({}),
            } as any),

          productCategoryService: () =>
            sinon.createStubInstance<ProductCategoryService>(ProductCategoryService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('load', () => {
      it('Should convert date from string', () => {
        // GIVEN
        const date = new Date('2019-10-15T11:42:02Z');

        // WHEN
        const convertedDate = comp.convertDateTimeFromServer(date);

        // THEN
        expect(convertedDate).toEqual(dayjs(date).format(DATE_TIME_LONG_FORMAT));
      });

      it('Should not convert date if date is not present', () => {
        expect(comp.convertDateTimeFromServer(null)).toBeNull();
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.product = entity;
        productServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.product = entity;
        productServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProduct = { id: 123 };
        productServiceStub.find.resolves(foundProduct);
        productServiceStub.retrieve.resolves([foundProduct]);

        // WHEN
        comp.beforeRouteEnter({ params: { productId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.product).toBe(foundProduct);
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
