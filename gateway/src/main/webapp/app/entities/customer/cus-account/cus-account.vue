<template>
  <div>
    <h2 id="page-heading" data-cy="CusAccountHeading">
      <span v-text="$t('gatewayApp.customerCusAccount.home.title')" id="cus-account-heading">Cus Accounts</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('gatewayApp.customerCusAccount.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'CusAccountCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-cus-account"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('gatewayApp.customerCusAccount.home.createLabel')"> Create a new Cus Account </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && cusAccounts && cusAccounts.length === 0">
      <span v-text="$t('gatewayApp.customerCusAccount.home.notFound')">No cusAccounts found</span>
    </div>
    <div class="table-responsive" v-if="cusAccounts && cusAccounts.length > 0">
      <table class="table table-striped" aria-describedby="cusAccounts">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.customerCusAccount.name')">Name</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.customerCusAccount.cardNo')">Card No</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.customerCusAccount.accountType')">Account Type</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.customerCusAccount.customer')">Customer</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cusAccount in cusAccounts" :key="cusAccount.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'CusAccountView', params: { cusAccountId: cusAccount.id } }">{{ cusAccount.id }}</router-link>
            </td>
            <td>{{ cusAccount.name }}</td>
            <td>{{ cusAccount.cardNo }}</td>
            <td v-text="$t('gatewayApp.AccountType.' + cusAccount.accountType)">{{ cusAccount.accountType }}</td>
            <td>
              <div v-if="cusAccount.customer">
                <router-link :to="{ name: 'CustomerView', params: { customerId: cusAccount.customer.id } }">{{
                  cusAccount.customer.id
                }}</router-link>
              </div>
            </td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'CusAccountView', params: { cusAccountId: cusAccount.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'CusAccountEdit', params: { cusAccountId: cusAccount.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(cusAccount)"
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
        ><span id="gatewayApp.customerCusAccount.delete.question" data-cy="cusAccountDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="au-delete-cusAccount-heading" v-text="$t('gatewayApp.customerCusAccount.delete.question', { id: removeId })">
          Are you sure you want to delete this Cus Account?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="au-confirm-delete-cusAccount"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeCusAccount()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./cus-account.component.ts"></script>
