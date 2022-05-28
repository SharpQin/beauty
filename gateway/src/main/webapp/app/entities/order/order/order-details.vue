<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="order">
        <h2 class="jh-entity-heading" data-cy="orderDetailsHeading">
          <span v-text="$t('gatewayApp.orderOrder.detail.title')">Order</span> {{ order.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.serialNo')">Serial No</span>
          </dt>
          <dd>
            <span>{{ order.serialNo }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.price')">Price</span>
          </dt>
          <dd>
            <span>{{ order.price }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.status')">Status</span>
          </dt>
          <dd>
            <span v-text="$t('gatewayApp.OrderStatus.' + order.status)">{{ order.status }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.custId')">Cust Id</span>
          </dt>
          <dd>
            <span>{{ order.custId }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.createdTime')">Created Time</span>
          </dt>
          <dd>
            <span v-if="order.createdTime">{{ $d(Date.parse(order.createdTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.actionTime')">Action Time</span>
          </dt>
          <dd>
            <span v-if="order.actionTime">{{ $d(Date.parse(order.actionTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.orderOrder.remark')">Remark</span>
          </dt>
          <dd>
            <span>{{ order.remark }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="order.id" :to="{ name: 'OrderEdit', params: { orderId: order.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./order-details.component.ts"></script>
