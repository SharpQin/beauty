<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.customerCusAccount.home.createOrEditLabel"
          data-cy="CusAccountCreateUpdateHeading"
          v-text="$t('gatewayApp.customerCusAccount.home.createOrEditLabel')"
        >
          Create or edit a CusAccount
        </h2>
        <div>
          <div class="form-group" v-if="cusAccount.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="cusAccount.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCusAccount.name')" for="cus-account-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="cus-account-name"
              data-cy="name"
              :class="{ valid: !$v.cusAccount.name.$invalid, invalid: $v.cusAccount.name.$invalid }"
              v-model="$v.cusAccount.name.$model"
              required
            />
            <div v-if="$v.cusAccount.name.$anyDirty && $v.cusAccount.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.cusAccount.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.cusAccount.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCusAccount.cardNo')" for="cus-account-cardNo">Card No</label>
            <input
              type="text"
              class="form-control"
              name="cardNo"
              id="cus-account-cardNo"
              data-cy="cardNo"
              :class="{ valid: !$v.cusAccount.cardNo.$invalid, invalid: $v.cusAccount.cardNo.$invalid }"
              v-model="$v.cusAccount.cardNo.$model"
              required
            />
            <div v-if="$v.cusAccount.cardNo.$anyDirty && $v.cusAccount.cardNo.$invalid">
              <small class="form-text text-danger" v-if="!$v.cusAccount.cardNo.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.cusAccount.cardNo.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 40 })"
              >
                This field cannot be longer than 40 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCusAccount.accountType')" for="cus-account-accountType"
              >Account Type</label
            >
            <select
              class="form-control"
              name="accountType"
              :class="{ valid: !$v.cusAccount.accountType.$invalid, invalid: $v.cusAccount.accountType.$invalid }"
              v-model="$v.cusAccount.accountType.$model"
              id="cus-account-accountType"
              data-cy="accountType"
            >
              <option
                v-for="accountType in accountTypeValues"
                :key="accountType"
                v-bind:value="accountType"
                v-bind:label="$t('gatewayApp.AccountType.' + accountType)"
              >
                {{ accountType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCusAccount.customer')" for="cus-account-customer"
              >Customer</label
            >
            <select class="form-control" id="cus-account-customer" data-cy="customer" name="customer" v-model="cusAccount.customer">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="cusAccount.customer && customerOption.id === cusAccount.customer.id ? cusAccount.customer : customerOption"
                v-for="customerOption in customers"
                :key="customerOption.id"
              >
                {{ customerOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.cusAccount.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./cus-account-update.component.ts"></script>
