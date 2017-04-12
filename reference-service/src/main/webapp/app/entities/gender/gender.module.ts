import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	GenderService,
	GenderPopupService,
	GenderComponent,
	GenderDetailComponent,
	GenderDialogComponent,
	GenderPopupComponent,
	GenderDeletePopupComponent,
	GenderDeleteDialogComponent,
	genderRoute,
	genderPopupRoute
} from "./";

let ENTITY_STATES = [
	...genderRoute,
	...genderPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		GenderComponent,
		GenderDetailComponent,
		GenderDialogComponent,
		GenderDeleteDialogComponent,
		GenderPopupComponent,
		GenderDeletePopupComponent,
	],
	entryComponents: [
		GenderComponent,
		GenderDialogComponent,
		GenderPopupComponent,
		GenderDeleteDialogComponent,
		GenderDeletePopupComponent,
	],
	providers: [
		GenderService,
		GenderPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceGenderModule {
}
