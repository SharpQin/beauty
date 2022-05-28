/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProductItemComponent from '@/entities/product/product-item/product-item.vue';
import ProductItemClass from '@/entities/product/product-item/product-item.component';
import ProductItemService from '@/entities/product/product-item/product-item.service';
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
  describe('ProductItem Management Component', () => {
    let wrapper: Wrapper<ProductItemClass>;
    let comp: ProductItemClass;
    let productItemServiceStub: SinonStubbedInstance<ProductItemService>;

    beforeEach(() => {
      productItemServiceStub = sinon.createStubInstance<ProductItemService>(ProductItemService);
      productItemServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ProductItemClass>(ProductItemComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          productItemService: () => productItemServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      productItemServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllProductItems();
      await comp.$nextTick();

      // THEN
      expect(productItemServiceStub.retrieve.called).toBeTruthy();
      expect(comp.productItems[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      productItemServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      expect(productItemServiceStub.retrieve.callCount).toEqual(1);

      comp.removeProductItem();
      await comp.$nextTick();

      // THEN
      expect(productItemServiceStub.delete.called).toBeTruthy();
      expect(productItemServiceStub.retrieve.callCount).toEqual(2);
    });
  });
});
