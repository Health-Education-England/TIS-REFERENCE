import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	NationalityService,
	NationalityPopupService,
	NationalityComponent,
	NationalityDetailComponent,
	NationalityDialogComponent,
	NationalityPopupComponent,
	NationalityDeletePopupComponent,
	NationalityDeleteDialogComponent,
	nationalityRoute,
	nationalityPopupRoute,
	NationalityResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...nationalityRoute,
	...nationalityPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		NationalityComponent,
		NationalityDetailComponent,
		NationalityDialogComponent,
		NationalityDeleteDialogComponent,
		NationalityPopupComponent,
		NationalityDeletePopupComponent,
	],
	entryComponents: [
		NationalityComponent,
		NationalityDialogComponent,
		NationalityPopupComponent,
		NationalityDeleteDialogComponent,
		NationalityDeletePopupComponent,
	],
	providers: [
		NationalityService,
		NationalityPopupService,
		NationalityResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceNationalityModule {
}
