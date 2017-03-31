import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {LeavingDestination} from "./leaving-destination.model";
import {LeavingDestinationPopupService} from "./leaving-destination-popup.service";
import {LeavingDestinationService} from "./leaving-destination.service";

@Component({
	selector: 'jhi-leaving-destination-delete-dialog',
	templateUrl: './leaving-destination-delete-dialog.component.html'
})
export class LeavingDestinationDeleteDialogComponent {

	leavingDestination: LeavingDestination;

	constructor(private jhiLanguageService: JhiLanguageService,
				private leavingDestinationService: LeavingDestinationService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['leavingDestination']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.leavingDestinationService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'leavingDestinationListModification',
				content: 'Deleted an leavingDestination'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-leaving-destination-delete-popup',
	template: ''
})
export class LeavingDestinationDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private leavingDestinationPopupService: LeavingDestinationPopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.leavingDestinationPopupService
				.open(LeavingDestinationDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
