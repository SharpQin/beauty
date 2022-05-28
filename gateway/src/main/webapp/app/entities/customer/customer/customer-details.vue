<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="customer">
        <h2 class="jh-entity-heading" data-cy="customerDetailsHeading">
          <span v-text="$t('gatewayApp.customerCustomer.detail.title')">Customer</span> {{ customer.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.name')">Name</span>
          </dt>
          <dd>
            <span>{{ customer.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.phone')">Phone</span>
          </dt>
          <dd>
            <span>{{ customer.phone }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.email')">Email</span>
          </dt>
          <dd>
            <span>{{ customer.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.birthday')">Birthday</span>
          </dt>
          <dd>
            <span>{{ customer.birthday }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.imageUrl')">Image Url</span>
          </dt>
          <dd>
            <span>{{ customer.imageUrl }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.langKey')">Lang Key</span>
          </dt>
          <dd>
            <span>{{ customer.langKey }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.vip')">Vip</span>
          </dt>
          <dd>
            <span>{{ customer.vip }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.level')">Level</span>
          </dt>
          <dd>
            <span>{{ customer.level }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.point')">Point</span>
          </dt>
          <dd>
            <span>{{ customer.point }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.activated')">Activated</span>
          </dt>
          <dd>
            <span>{{ customer.activated }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.createdDate')">Created Date</span>
          </dt>
          <dd>
            <span v-if="customer.createdDate">{{ $d(Date.parse(customer.createdDate), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCustomer.lastModifiedDate')">Last Modified Date</span>
          </dt>
          <dd>
            <span v-if="customer.lastModifiedDate">{{ $d(Date.parse(customer.lastModifiedDate), 'long') }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="customer.id" :to="{ name: 'CustomerEdit', params: { customerId: customer.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./customer-details.component.ts"></script>
