<template>
  <div>
    <h2 id="page-heading" data-cy="OrderItemHeading">
      <span v-text="$t('gatewayApp.orderOrderItem.home.title')" id="order-item-heading">Order Items</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('gatewayApp.orderOrderItem.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'OrderItemCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-order-item"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('gatewayApp.orderOrderItem.home.createLabel')"> Create a new Order Item </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && orderItems && orderItems.length === 0">
      <span v-text="$t('gatewayApp.orderOrderItem.home.notFound')">No orderItems found</span>
    </div>
    <div class="table-responsive" v-if="orderItems && orderItems.length > 0">
      <table class="table table-striped" aria-describedby="orderItems">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.orderOrderItem.prodId')">Prod Id</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.orderOrderItem.quantity')">Quantity</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.orderOrderItem.totalPrice')">Total Price</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.orderOrderItem.price')">Price</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.orderOrderItem.status')">Status</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.orderOrderItem.order')">Order</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="orderItem in orderItems" :key="orderItem.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'OrderItemView', params: { orderItemId: orderItem.id } }">{{ orderItem.id }}</router-link>
            </td>
            <td>{{ orderItem.prodId }}</td>
            <td>{{ orderItem.quantity }}</td>
            <td>{{ orderItem.totalPrice }}</td>
            <td>{{ orderItem.price }}</td>
            <td v-text="$t('gatewayApp.OrderItemStatus.' + orderItem.status)">{{ orderItem.status }}</td>
            <td>
              <div v-if="orderItem.order">
                <router-link :to="{ name: 'OrderView', params: { orderId: orderItem.order.id } }">{{ orderItem.order.id }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'OrderItemView', params: { orderItemId: orderItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'OrderItemEdit', params: { orderItemId: orderItem.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(orderItem)"
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
        ><span id="gatewayApp.orderOrderItem.delete.question" data-cy="orderItemDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="au-delete-orderItem-heading" v-text="$t('gatewayApp.orderOrderItem.delete.question', { id: removeId })">
          Are you sure you want to delete this Order Item?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="au-confirm-delete-orderItem"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeOrderItem()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./order-item.component.ts"></script>
