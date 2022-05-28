<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="orderItem">
        <h2 class="jh-entity-heading" data-cy="orderItemDetailsHeading">
          <span v-text="$t('gatewayApp.orderOrderItem.detail.title')">OrderItem</span> {{ orderItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gatewayApp.orderOrderItem.prodId')">Prod Id</span>
          </dt>
          <dd>
            <span>{{ orderItem.prodId }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrderItem.quantity')">Quantity</span>
          </dt>
          <dd>
            <span>{{ orderItem.quantity }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrderItem.totalPrice')">Total Price</span>
          </dt>
          <dd>
            <span>{{ orderItem.totalPrice }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrderItem.price')">Price</span>
          </dt>
          <dd>
            <span>{{ orderItem.price }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrderItem.status')">Status</span>
          </dt>
          <dd>
            <span v-text="$t('gatewayApp.OrderItemStatus.' + orderItem.status)">{{ orderItem.status }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrderItem.order')">Order</span>
          </dt>
          <dd>
            <div v-if="orderItem.order">
              <router-link :to="{ name: 'OrderView', params: { orderId: orderItem.order.id } }">{{ orderItem.order.id }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="orderItem.id"
          :to="{ name: 'OrderItemEdit', params: { orderItemId: orderItem.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./order-item-details.component.ts"></script>
