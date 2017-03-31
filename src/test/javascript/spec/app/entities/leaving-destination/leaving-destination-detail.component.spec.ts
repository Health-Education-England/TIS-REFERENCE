import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {LeavingDestinationDetailComponent} from "../../../../../../main/webapp/app/entities/leaving-destination/leaving-destination-detail.component";
import {LeavingDestinationService} from "../../../../../../main/webapp/app/entities/leaving-destination/leaving-destination.service";
import {LeavingDestination} from "../../../../../../main/webapp/app/entities/leaving-destination/leaving-destination.model";

describe('Component Tests', () => {

	describe('LeavingDestination Management Detail Component', () => {
		let comp: LeavingDestinationDetailComponent;
		let fixture: ComponentFixture<LeavingDestinationDetailComponent>;
		let service: LeavingDestinationService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [LeavingDestinationDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					LeavingDestinationService
				]
			}).overrideComponent(LeavingDestinationDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(LeavingDestinationDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(LeavingDestinationService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new LeavingDestination(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.leavingDestination).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
