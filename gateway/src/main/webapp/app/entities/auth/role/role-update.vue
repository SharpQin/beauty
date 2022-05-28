<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.authRole.home.createOrEditLabel"
          data-cy="RoleCreateUpdateHeading"
          v-text="$t('gatewayApp.authRole.home.createOrEditLabel')"
        >
          Create or edit a Role
        </h2>
        <div>
          <div class="form-group" v-if="role.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="role.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authRole.name')" for="role-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="role-name"
              data-cy="name"
              :class="{ valid: !$v.role.name.$invalid, invalid: $v.role.name.$invalid }"
              v-model="$v.role.name.$model"
              required
            />
            <div v-if="$v.role.name.$anyDirty && $v.role.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.role.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.role.name.maxLength" v-text="$t('entity.validation.maxlength', { max: 30 })">
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.authRole.dsc')" for="role-dsc">Dsc</label>
            <input
              type="text"
              class="form-control"
              name="dsc"
              id="role-dsc"
              data-cy="dsc"
              :class="{ valid: !$v.role.dsc.$invalid, invalid: $v.role.dsc.$invalid }"
              v-model="$v.role.dsc.$model"
            />
          </div>
          <div class="form-group">
            <label v-text="$t('gatewayApp.authRole.permissions')" for="role-permissions">Permissions</label>
            <select
              class="form-control"
              id="role-permissions"
              data-cy="permissions"
              multiple
              name="permissions"
              v-if="role.permissions !== undefined"
              v-model="role.permissions"
              required
            >
              <option
                v-bind:value="getSelected(role.permissions, permissionOption)"
                v-for="permissionOption in permissions"
                :key="permissionOption.id"
              >
                {{ permissionOption.id }}
              </option>
            </select>
          </div>
          <div v-if="$v.role.permissions.$anyDirty && $v.role.permissions.$invalid">
            <small class="form-text text-danger" v-if="!$v.role.permissions.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
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
            :disabled="$v.role.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./role-update.component.ts"></script>
