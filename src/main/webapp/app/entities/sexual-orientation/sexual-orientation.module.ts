import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	SexualOrientationService,
	SexualOrientationPopupService,
	SexualOrientationComponent,
	SexualOrientationDetailComponent,
	SexualOrientationDialogComponent,
	SexualOrientationPopupComponent,
	SexualOrientationDeletePopupComponent,
	SexualOrientationDeleteDialogComponent,
	sexualOrientationRoute,
	sexualOrientationPopupRoute
} from "./";

let ENTITY_STATES = [
	...sexualOrientationRoute,
	...sexualOrientationPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		SexualOrientationComponent,
		SexualOrientationDetailComponent,
		SexualOrientationDialogComponent,
		SexualOrientationDeleteDialogComponent,
		SexualOrientationPopupComponent,
		SexualOrientationDeletePopupComponent,
	],
	entryComponents: [
		SexualOrientationComponent,
		SexualOrientationDialogComponent,
		SexualOrientationPopupComponent,
		SexualOrientationDeleteDialogComponent,
		SexualOrientationDeletePopupComponent,
	],
	providers: [
		SexualOrientationService,
		SexualOrientationPopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceSexualOrientationModule {
}
