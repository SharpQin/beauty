<template>
  <div>
    <h2 id="page-heading" data-cy="ProductItemHeading">
      <span v-text="$t('gatewayApp.productProductItem.home.title')" id="product-item-heading">Product Items</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('gatewayApp.productProductItem.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProductItemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-product-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('gatewayApp.productProductItem.home.createLabel')"> Create a new Product Item </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && productItems && productItems.length === 0">
      <span v-text="$t('gatewayApp.productProductItem.home.notFound')">No productItems found</span>
    </div>
    <div class="table-responsive" v-if="productItems && productItems.length > 0">
      <table class="table table-striped" aria-describedby="productItems">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.productProductItem.name')">Name</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.productProductItem.amount')">Amount</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.productProductItem.image')">Image</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.productProductItem.dsc')">Dsc</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.productProductItem.product')">Product</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="productItem in productItems" :key="productItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProductItemView', params: { productItemId: productItem.id } }">{{ productItem.id }}</router-link>
            </td>
            <td>{{ productItem.name }}</td>
            <td>{{ productItem.amount }}</td>
            <td>{{ productItem.image }}</td>
            <td>{{ productItem.dsc }}</td>
            <td>
              <div v-if="productItem.product">
                <router-link :to="{ name: 'ProductView', params: { productId: productItem.product.id } }">{{
                  productItem.product.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ProductItemView', params: { productItemId: productItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProductItemEdit', params: { productItemId: productItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(productItem)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span
          id="gatewayApp.productProductItem.delete.question"
          data-cy="productItemDeleteDialogHeading"
          v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="au-delete-productItem-heading" v-text="$t('gatewayApp.productProductItem.delete.question', { id: removeId })">
          Are you sure you want to delete this Product Item?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="au-confirm-delete-productItem"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeProductItem()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./product-item.component.ts"></script>
