<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.authPermission.home.createOrEditLabel"
          data-cy="PermissionCreateUpdateHeading"
          v-text="$t('gatewayApp.authPermission.home.createOrEditLabel')"
        >
          Create or edit a Permission
        </h2>
        <div>
          <div class="form-group" v-if="permission.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="permission.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authPermission.authKey')" for="permission-authKey">Auth Key</label>
            <input
              type="text"
              class="form-control"
              name="authKey"
              id="permission-authKey"
              data-cy="authKey"
              :class="{ valid: !$v.permission.authKey.$invalid, invalid: $v.permission.authKey.$invalid }"
              v-model="$v.permission.authKey.$model"
              required
            />
            <div v-if="$v.permission.authKey.$anyDirty && $v.permission.authKey.$invalid">
              <small class="form-text text-danger" v-if="!$v.permission.authKey.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.permission.authKey.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 30 })"
              >
                This field cannot be longer than 30 characters.
              </small>
            </div>
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
            :disabled="$v.permission.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./permission-update.component.ts"></script>
