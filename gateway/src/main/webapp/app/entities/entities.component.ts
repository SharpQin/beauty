import { Component, Provide, Vue } from 'vue-property-decorator';

import UserService from '@/entities/user/user.service';
import AddressService from './customer/address/address.service';
import CusAccountService from './customer/cus-account/cus-account.service';
import CustomerService from './customer/customer/customer.service';
import MenuService from './auth/menu/menu.service';
import OrderService from './order/order/order.service';
import OrderItemService from './order/order-item/order-item.service';
import PermissionService from './auth/permission/permission.service';
import ProductService from './product/product/product.service';
import ProductCategoryService from './product/product-category/product-category.service';
import ProductItemService from './product/product-item/product-item.service';
import RoleService from './auth/role/role.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

@Component
export default class Entities extends Vue {
  @Provide('userService') private userService = () => new UserService();
  @Provide('addressService') private addressService = () => new AddressService();
  @Provide('cusAccountService') private cusAccountService = () => new CusAccountService();
  @Provide('customerService') private customerService = () => new CustomerService();
  @Provide('menuService') private menuService = () => new MenuService();
  @Provide('orderService') private orderService = () => new OrderService();
  @Provide('orderItemService') private orderItemService = () => new OrderItemService();
  @Provide('permissionService') private permissionService = () => new PermissionService();
  @Provide('productService') private productService = () => new ProductService();
  @Provide('productCategoryService') private productCategoryService = () => new ProductCategoryService();
  @Provide('productItemService') private productItemService = () => new ProductItemService();
  @Provide('roleService') private roleService = () => new RoleService();
  // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
}
