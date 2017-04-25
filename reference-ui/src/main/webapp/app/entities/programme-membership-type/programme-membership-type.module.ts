import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	ProgrammeMembershipTypeService,
	ProgrammeMembershipTypePopupService,
	ProgrammeMembershipTypeComponent,
	ProgrammeMembershipTypeDetailComponent,
	ProgrammeMembershipTypeDialogComponent,
	ProgrammeMembershipTypePopupComponent,
	ProgrammeMembershipTypeDeletePopupComponent,
	ProgrammeMembershipTypeDeleteDialogComponent,
	programmeMembershipTypeRoute,
	programmeMembershipTypePopupRoute
} from "./";

let ENTITY_STATES = [
	...programmeMembershipTypeRoute,
	...programmeMembershipTypePopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		ProgrammeMembershipTypeComponent,
		ProgrammeMembershipTypeDetailComponent,
		ProgrammeMembershipTypeDialogComponent,
		ProgrammeMembershipTypeDeleteDialogComponent,
		ProgrammeMembershipTypePopupComponent,
		ProgrammeMembershipTypeDeletePopupComponent,
	],
	entryComponents: [
		ProgrammeMembershipTypeComponent,
		ProgrammeMembershipTypeDialogComponent,
		ProgrammeMembershipTypePopupComponent,
		ProgrammeMembershipTypeDeleteDialogComponent,
		ProgrammeMembershipTypeDeletePopupComponent,
	],
	providers: [
		ProgrammeMembershipTypeService,
		ProgrammeMembershipTypePopupService,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceProgrammeMembershipTypeModule {
}
