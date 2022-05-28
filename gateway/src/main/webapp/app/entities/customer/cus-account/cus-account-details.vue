<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="cusAccount">
        <h2 class="jh-entity-heading" data-cy="cusAccountDetailsHeading">
          <span v-text="$t('gatewayApp.customerCusAccount.detail.title')">CusAccount</span> {{ cusAccount.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gatewayApp.customerCusAccount.name')">Name</span>
          </dt>
          <dd>
            <span>{{ cusAccount.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCusAccount.cardNo')">Card No</span>
          </dt>
          <dd>
            <span>{{ cusAccount.cardNo }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCusAccount.accountType')">Account Type</span>
          </dt>
          <dd>
            <span v-text="$t('gatewayApp.AccountType.' + cusAccount.accountType)">{{ cusAccount.accountType }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.customerCusAccount.customer')">Customer</span>
          </dt>
          <dd>
            <div v-if="cusAccount.customer">
              <router-link :to="{ name: 'CustomerView', params: { customerId: cusAccount.customer.id } }">{{
                cusAccount.customer.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="cusAccount.id"
          :to="{ name: 'CusAccountEdit', params: { cusAccountId: cusAccount.id } }"
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

<script lang="ts" src="./cus-account-details.component.ts"></script>
